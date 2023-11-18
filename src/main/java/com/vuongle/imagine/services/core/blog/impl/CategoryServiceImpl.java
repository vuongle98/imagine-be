package com.vuongle.imagine.services.core.blog.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.dto.blog.CategoryDto;
import com.vuongle.imagine.dto.common.CommonResult;
import com.vuongle.imagine.exceptions.NoPermissionException;
import com.vuongle.imagine.models.Category;
import com.vuongle.imagine.models.Post;
import com.vuongle.imagine.repositories.CategoryRepository;
import com.vuongle.imagine.services.core.blog.CategoryService;
import com.vuongle.imagine.services.core.blog.PostService;
import com.vuongle.imagine.services.core.blog.command.CreateCategoryCommand;
import com.vuongle.imagine.services.core.blog.command.UpdateCategoryCommand;
import com.vuongle.imagine.services.share.blog.CategoryQueryService;
import com.vuongle.imagine.utils.Context;
import com.vuongle.imagine.utils.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ObjectMapper objectMapper;

    private final CategoryRepository categoryRepository;

    private final CategoryQueryService categoryQueryService;

    private final PostService postService;

    public CategoryServiceImpl(
            ObjectMapper objectMapper,
            CategoryRepository categoryRepository,
            CategoryQueryService categoryQueryService,
            PostService postService
    ) {
        this.objectMapper = objectMapper;
        this.categoryRepository = categoryRepository;
        this.categoryQueryService = categoryQueryService;
        this.postService = postService;
    }

    @Override
    public CategoryDto create(CreateCategoryCommand command) {

        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        // map value
        Category category = objectMapper.convertValue(command, Category.class);
        category.setSlug(StringUtils.toSlug(category.getName()));

        category = categoryRepository.save(category);
        return categoryQueryService.getById(category.getId(), CategoryDto.class);
    }

    @Override
    public CategoryDto update(UpdateCategoryCommand command) {

        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        // find existed
        Category existed = categoryQueryService.getById(command.getId(), Category.class);

        boolean modified = false;

        if (Objects.nonNull(command.getName())) {
            existed.setName(command.getName());
            existed.setSlug(StringUtils.toSlug(existed.getName()));

            modified = true;
        }

        if (command.isRecover()) {
            existed.setDeletedAt(null);

            postService.recoverByCategoryId(existed.getId());

            modified = true;
        }

        if (modified) {
            existed = categoryRepository.save(existed);

            // update post relate category
            updatePostByCategory(existed.getId());
        }
        return categoryQueryService.getById(existed.getId(), CategoryDto.class);
    }

    @Override
    public void delete(ObjectId id, boolean delete) {

        // validate is admin
        if (!Context.hasModifyPermission()) {
            throw new NoPermissionException("No permission");
        }

        if (delete) {
            // delete category and post relate
            postService.deleteByCategoryId(id, true);
            categoryRepository.deleteById(id);
        } else {
            // find category
            Category category = categoryQueryService.getById(id, Category.class);

            category.setDeletedAt(Instant.now());

            categoryRepository.save(category);

            // update post
            postService.deleteByCategoryId(category.getId(), false);
        }
    }

    private void updatePostByCategory(ObjectId categoryId) {
        // update post relate category
        CommonResult<Post> result = postService.updateCategory(categoryId);
    }
}
