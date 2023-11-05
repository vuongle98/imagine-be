package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.blog.PostDto;
import com.vuongle.imagine.services.core.blog.PostService;
import com.vuongle.imagine.services.core.blog.command.CreatePostCommand;
import com.vuongle.imagine.services.core.blog.command.UpdatePostCommand;
import com.vuongle.imagine.services.share.blog.PostQueryService;
import com.vuongle.imagine.services.share.blog.query.PostQuery;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostQueryService postQueryService;

    private final PostService postService;

    public PostController(PostQueryService postQueryService, PostService postService) {
        this.postQueryService = postQueryService;
        this.postService = postService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Page<PostDto>> findPagePost(
        PostQuery postQuery,
        Pageable pageable
    ) {
        // log request uri

        Page<PostDto> posts = postQueryService.findPage(postQuery, pageable, PostDto.class);

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<PostDto> create(
        @RequestBody @Valid CreatePostCommand command
    ) {
        // log request uri

        PostDto createdPost = postService.create(command);

        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Void> delete(
        @PathVariable(value = "id") ObjectId id
    ) {
        // log request uri
        postService.delete(id, false);

        return ResponseEntity.ok(null);
    }
}
