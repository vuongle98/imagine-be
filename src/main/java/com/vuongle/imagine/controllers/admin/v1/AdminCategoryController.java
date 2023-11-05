package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.dto.blog.CategoryDto;
import com.vuongle.imagine.services.core.blog.CategoryService;
import com.vuongle.imagine.services.core.blog.command.CreateCategoryCommand;
import com.vuongle.imagine.services.core.blog.command.UpdateCategoryCommand;
import com.vuongle.imagine.services.share.blog.CategoryQueryService;
import com.vuongle.imagine.services.share.blog.query.CategoryQuery;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/category")
@Slf4j
public class AdminCategoryController {

    private final CategoryService categoryService;

    private final CategoryQueryService categoryQueryService;

    public AdminCategoryController(
            CategoryService categoryService,
            CategoryQueryService categoryQueryService
    ) {
        this.categoryService = categoryService;
        this.categoryQueryService = categoryQueryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Page<CategoryDto>> findPageCategory(
            CategoryQuery categoryQuery,
            Pageable pageable
    ) {
        // log request uri

        Page<CategoryDto> data = categoryQueryService.findPage(categoryQuery, pageable, CategoryDto.class);

        return ResponseEntity.ok(data);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<CategoryDto> create(
            @RequestBody @Valid CreateCategoryCommand command
    ) {
        // log request uri

        CategoryDto data = categoryService.create(command);

        return ResponseEntity.ok(data);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<CategoryDto> update(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid UpdateCategoryCommand command
    ) {
        // log request uri
        command.setId(id);
        CategoryDto data = categoryService.update(command);

        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Void> delete(
            @PathVariable(value = "id") ObjectId id,
            @RequestParam(value = "force", required = false) boolean force
    ) {
        // log request uri
        categoryService.delete(id, force);

        return ResponseEntity.ok(null);
    }
}
