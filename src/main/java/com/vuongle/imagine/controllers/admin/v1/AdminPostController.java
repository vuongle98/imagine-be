package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.dto.blog.PostDto;
import com.vuongle.imagine.services.core.blog.PostService;
import com.vuongle.imagine.services.core.blog.command.CreatePostCommand;
import com.vuongle.imagine.services.core.blog.command.UpdatePostCommand;
import com.vuongle.imagine.services.share.blog.PostQueryService;
import com.vuongle.imagine.services.share.blog.query.PostQuery;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/post")
@Slf4j
public class AdminPostController {

    private final PostQueryService postQueryService;

    private final PostService postService;

    public AdminPostController(
            PostQueryService postQueryService,
            PostService postService
    ) {
        this.postQueryService = postQueryService;
        this.postService = postService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Page<PostDto>> findPagePost(
            PostQuery postQuery,
            Pageable pageable
    ) {
        // log request uri

        // init lookup
        AggregationOperation lookUp = Aggregation.lookup(
                "comment",
                "_id",
                "postId",
                "comments"
        );

        Page<PostDto> posts = postQueryService.findPage(postQuery, pageable, PostDto.class, lookUp);

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<PostDto> create(
            @RequestBody @Valid CreatePostCommand command
    ) {
        PostDto post = postService.create(command);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<PostDto> findById(
            @PathVariable(value = "id") ObjectId id
    ) {
        // log request uri

        PostDto data = postQueryService.getById(id);

        return ResponseEntity.ok(data);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<PostDto> update(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid UpdatePostCommand command
    ) {
        // log request uri
        command.setId(id);
        PostDto data = postService.update(command);

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
        postService.delete(id, force);

        return ResponseEntity.ok(null);
    }

    @PutMapping("/{id}/--recover")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Void> recover(
            @PathVariable(value = "id") ObjectId id
    ) {
        // log request uri
        postService.recoverByCategoryId(id);

        return ResponseEntity.ok(null);
    }
}
