package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.blog.CommentDto;
import com.vuongle.imagine.services.core.blog.CommentService;
import com.vuongle.imagine.services.core.blog.command.CreateCommentCommand;
import com.vuongle.imagine.services.core.blog.command.UpdateCommentCommand;
import com.vuongle.imagine.services.share.blog.CommentQueryService;
import com.vuongle.imagine.services.share.blog.query.CommentQuery;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentQueryService commentQueryService;

    private final CommentService commentService;

    public CommentController(CommentQueryService commentQueryService, CommentService commentService) {
        this.commentQueryService = commentQueryService;
        this.commentService = commentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Page<CommentDto>> findPageComment(
        CommentQuery commentQuery,
        Pageable pageable
    ) {
        // log request uri

        Page<CommentDto> comments = commentQueryService.findPage(commentQuery, pageable, CommentDto.class);

        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<CommentDto> create(
        @RequestBody @Valid CreateCommentCommand command
    ) {
        // log request uri

        CommentDto createdComment = commentService.create(command);

        return ResponseEntity.ok(createdComment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<CommentDto> update(
        @PathVariable(value = "id") ObjectId id,
        @RequestBody @Valid UpdateCommentCommand command
    ) {
        // log request uri
        command.setId(id);
        CommentDto data = commentService.update(command);

        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Void> delete(
        @PathVariable(value = "id") ObjectId id
    ) {
        // log request uri
        commentService.delete(id, false);

        return ResponseEntity.ok(null);
    }
}
