package com.vuongle.imagine.services.core.blog.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.dto.blog.CategoryDto;
import com.vuongle.imagine.dto.blog.PostDto;
import com.vuongle.imagine.dto.common.CommonResult;
import com.vuongle.imagine.dto.common.Result;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.exceptions.NoPermissionException;
import com.vuongle.imagine.exceptions.UserNotFoundException;
import com.vuongle.imagine.models.Post;
import com.vuongle.imagine.models.embeded.Creator;
import com.vuongle.imagine.repositories.PostRepository;
import com.vuongle.imagine.services.core.blog.CommentService;
import com.vuongle.imagine.services.core.blog.PostService;
import com.vuongle.imagine.services.core.blog.command.CreatePostCommand;
import com.vuongle.imagine.services.core.blog.command.UpdatePostCommand;
import com.vuongle.imagine.services.share.blog.CategoryQueryService;
import com.vuongle.imagine.services.share.blog.PostQueryService;
import com.vuongle.imagine.services.share.blog.query.PostQuery;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {

    private final ObjectMapper objectMapper;

    private final CategoryQueryService categoryQueryService;

    private final PostQueryService postQueryService;

    private final PostRepository postRepository;

    private final CommentService commentService;


    public PostServiceImpl(
            ObjectMapper objectMapper,
            CategoryQueryService categoryQueryService,
            PostQueryService postQueryService,
            PostRepository postRepository,
            CommentService commentService
    ) {
        this.objectMapper = objectMapper;
        this.categoryQueryService = categoryQueryService;
        this.postQueryService = postQueryService;
        this.postRepository = postRepository;
        this.commentService = commentService;
    }

    @Override
    public PostDto create(CreatePostCommand command) {

        // map value
        Post post = objectMapper.convertValue(command, Post.class);

        // set category
        CategoryDto category = categoryQueryService.getById(command.getCategoryId(), CategoryDto.class);

        if (Objects.isNull(category)) {
            throw new DataNotFoundException("Category not found");
        }

        post.setCategory(category);

        // set creator
        Creator user = Context.getCreator();

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        post.setCreator(user);

        // save post
        post = postRepository.save(post);

        return postQueryService.getById(post.getId(), PostDto.class);
    }

    @Override
    public PostDto update(UpdatePostCommand command) {

        Post post = postQueryService.getById(command.getId(), Post.class);

        boolean hasModified = false;

        if (!validateAuthorPost(post)) {
            throw new NoPermissionException("No permission");
        }

        if (Objects.nonNull(command.getCategoryId())) {
            CategoryDto category = categoryQueryService.getById(command.getCategoryId(), CategoryDto.class);

            if (Objects.isNull(category)) {
                throw new DataNotFoundException("Category not found");
            }

            post.setCategory(category);
            hasModified = true;
        }

        if (Objects.nonNull(command.getTitle())) {
            post.setTitle(command.getTitle());
            hasModified = true;
        }

        if (Objects.nonNull(command.getDescription())) {
            post.setDescription(command.getDescription());
            hasModified = true;
        }

        if (Objects.nonNull(command.getFile())) {
            post.setFile(command.getFile());
            hasModified = true;
        }

        if (Objects.nonNull(command.getContent())) {
            post.setContent(command.getContent());
            hasModified = true;
        }

        if (Context.hasModifyPermission() && command.isRecover()) {
            post.setDeletedAt(null);

            // recover comment
            commentService.recoverByPostId(post.getId());
            hasModified = true;
        }

        if (hasModified) {
            post = postRepository.save(post);
        }

        return postQueryService.getById(post.getId(), PostDto.class);
    }

    @Override
    public void delete(ObjectId id, boolean delete) {

        Post post = postQueryService.getById(id, Post.class);

        if (!validateAuthorPost(post) || !Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        // delete post and its comment
        if (delete && Context.hasModifyPermission()) {
            commentService.deleteByPostId(id, true);
            postRepository.deleteById(id);
        } else {
            // find post
            post.setDeletedAt(Instant.now());

            postRepository.save(post);

            // delete all comment
            commentService.deleteByPostId(id, false);
        }
    }

    @Override
    public void deleteByCategoryId(ObjectId categoryId, boolean isDelete) {

        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        if (isDelete) {

            // delete all comment
            commentService.deleteByCategoryId(categoryId, true);
            postRepository.deleteAllByCategoryId(categoryId);

        } else {
            // set post status is deleted

            PostQuery postQuery = new PostQuery();
            postQuery.setCategoryId(categoryId);

            List<Post> posts = postQueryService.findList(postQuery, Post.class);

            for (Post post : posts) {
                post.setDeletedAt(Instant.now());

                // update comment
                commentService.deleteByCategoryId(categoryId, false);
            }

            postRepository.saveAll(posts);
        }
    }

    @Override
    public void recoverByCategoryId(ObjectId categoryId) {
        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        PostQuery postQuery = new PostQuery();
        postQuery.setCategoryId(categoryId);
        postQuery.setGetDeleted(true);

        List<Post> posts = postQueryService.findList(postQuery, Post.class);

        for (Post post : posts) {
            post.setDeletedAt(null);
        }

        postRepository.saveAll(posts);
    }

    @Override
    public void recover(ObjectId id) {
        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        Post post = postQueryService.getById(id, Post.class);
        post.setDeletedAt(null);

        postRepository.save(post);
    }

    @Override
    public CommonResult<Post> updateCategory(ObjectId categoryId) {

        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        CommonResult<Post> result = new CommonResult<>();
        // find all post by category
        PostQuery postQuery = new PostQuery();
        postQuery.setCategoryId(categoryId);
        List<Post> posts = postQueryService.findList(postQuery, Post.class);

        List<Result<Post>> results = new ArrayList<>();

        // if large data, using bulk update
        for (Post post : posts) {
            post.setCategory(categoryQueryService.getById(categoryId, CategoryDto.class));

            Result<Post> r = new Result<>();
            r.setSuccess(true);
            r.setResult(post);

            results.add(r);
        }

        postRepository.saveAll(posts);

        result.setResults(results);
        result.setSuccessCount(results.stream().filter(Result::isSuccess).count());

        return result;
    }

    private boolean validateAuthorPost(Post post) {
        if (Objects.isNull(post)) {
            throw new DataNotFoundException("Post not found");
        }

        if (Objects.isNull(Context.getCreator())) throw new UserNotFoundException("User not found");

        if (!Context.getCreator().getId().equals(post.getCreator().getId())) {
            throw new NoPermissionException("No permission");
        }

        return true;
    }
}
