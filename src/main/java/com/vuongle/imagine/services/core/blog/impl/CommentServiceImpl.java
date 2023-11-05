package com.vuongle.imagine.services.core.blog.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.dto.blog.CommentDto;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.exceptions.NoPermissionException;
import com.vuongle.imagine.exceptions.UserNotFoundException;
import com.vuongle.imagine.models.Comment;
import com.vuongle.imagine.models.Post;
import com.vuongle.imagine.models.embeded.Creator;
import com.vuongle.imagine.repositories.CommentRepository;
import com.vuongle.imagine.services.core.blog.CommentService;
import com.vuongle.imagine.services.core.blog.command.CreateCommentCommand;
import com.vuongle.imagine.services.core.blog.command.UpdateCommentCommand;
import com.vuongle.imagine.services.share.blog.CommentQueryService;
import com.vuongle.imagine.services.share.blog.PostQueryService;
import com.vuongle.imagine.services.share.blog.query.CommentQuery;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ObjectMapper objectMapper;

    private final CommentQueryService commentQueryService;

    private final PostQueryService postQueryService;


    public CommentServiceImpl(
            CommentRepository commentRepository,
            ObjectMapper objectMapper,
            CommentQueryService commentQueryService,
            PostQueryService postQueryService
    ) {
        this.commentRepository = commentRepository;
        this.objectMapper = objectMapper;
        this.commentQueryService = commentQueryService;
        this.postQueryService = postQueryService;
    }

    @Override
    public CommentDto create(CreateCommentCommand command) {

        // logged in user can comment

        Comment comment = objectMapper.convertValue(command, Comment.class);

        Creator creator = Context.getCreator();

        if (Objects.isNull(creator)) throw new NoPermissionException("User has no permission");

        comment.setCreator(creator);

        comment = commentRepository.save(comment);

        return commentQueryService.getById(comment.getId(), CommentDto.class);
    }

    @Override
    public CommentDto update(UpdateCommentCommand command) {

        // created comment can update

        Comment comment = commentQueryService.getById(command.getId(), Comment.class);

        if (!validateComment(comment)) throw new NoPermissionException("No permission");

        if (Objects.nonNull(command.getFile())) {
            comment.setFile(command.getFile());
        }

        if (Objects.nonNull(command.getContent())) {
            comment.setContent(command.getContent());
        }

        comment = commentRepository.save(comment);

        return commentQueryService.getById(comment.getId(), CommentDto.class);
    }

    @Override
    public void delete(ObjectId id, boolean delete) {

        // created comment can delete

        // validate user's comment
        Comment comment = commentQueryService.getById(id, Comment.class);

        if (!validateComment(comment) && !Context.hasModifyPermission()) throw new NoPermissionException("No permission");

        // admin can force delete, other just update
        if (delete && Context.hasModifyPermission()) {
            commentRepository.deleteById(id);
        } else {
            // set delete status
            comment.setDeletedAt(Instant.now());

            commentRepository.save(comment);
        }
    }


    /**
     * deleteByPostId: delete comment by post id
     * permission: admin can force delete, other just update status
     * @param postId {@link ObjectId}
     * @param isDelete {@link Boolean} - true: force delete, false: update
     */
    @Override
    public void deleteByPostId(ObjectId postId, boolean isDelete) {

        Post post = postQueryService.getById(postId, Post.class);

        if (!validateAuthorPost(post) || !Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        // admin can force delete
        if (isDelete && Context.hasModifyPermission()) {
            commentRepository.deleteAllByPostId(postId);
        } else {

            CommentQuery commentQuery = new CommentQuery();
            commentQuery.setPostId(postId);

            List<Comment> comments = commentQueryService.findList(commentQuery, Comment.class);

            for (Comment c : comments) {
                c.setDeletedAt(Instant.now());
            }

            commentRepository.saveAll(comments);
        }
    }

    @Override
    public void deleteByCategoryId(ObjectId categoryId, boolean isDelete) {

        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        if (isDelete) {
            commentRepository.deleteAllByCategoryId(categoryId);
        } else {

            CommentQuery commentQuery = new CommentQuery();
            commentQuery.setCategoryId(categoryId);

            List<Comment> comments = commentQueryService.findList(commentQuery, Comment.class);

            for (Comment comment : comments) {
                comment.setDeletedAt(Instant.now());
            }

            commentRepository.saveAll(comments);
        }
    }

    @Override
    public void recoverByPostId(ObjectId postId) {

        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setPostId(postId);
        commentQuery.setGetDeleted(true);

        List<Comment> comments = commentQueryService.findList(commentQuery, Comment.class);

        for (Comment comment : comments) {
            comment.setDeletedAt(null);
        }

        commentRepository.saveAll(comments);
    }

    @Override
    public void recoverByCategoryId(ObjectId categoryId) {

        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setCategoryId(categoryId);
        commentQuery.setGetDeleted(true);

        List<Comment> comments = commentQueryService.findList(commentQuery, Comment.class);

        for (Comment comment : comments) {
            comment.setDeletedAt(null);
        }

        commentRepository.saveAll(comments);
    }

    private boolean validateComment(Comment comment) {
        if (Objects.isNull(comment)) {
            throw new DataNotFoundException("Comment not found");
        }

        if (Objects.isNull(Context.getCreator())) throw new UserNotFoundException("User not found");

        if (!Context.getCreator().getId().equals(comment.getCreator().getId())) {
            throw new NoPermissionException("No permission");
        }

        return true;
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
