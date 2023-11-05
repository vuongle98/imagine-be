package com.vuongle.imagine.services.core.blog;

import com.vuongle.imagine.dto.blog.CategoryDto;
import com.vuongle.imagine.services.core.BaseService;
import com.vuongle.imagine.services.core.blog.command.CreateCategoryCommand;
import com.vuongle.imagine.services.core.blog.command.UpdateCategoryCommand;

public interface CategoryService extends BaseService<CreateCategoryCommand, UpdateCategoryCommand, CategoryDto> {
}
