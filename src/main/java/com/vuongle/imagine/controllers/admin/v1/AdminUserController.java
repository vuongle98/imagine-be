package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.services.core.auth.command.RegisterCommand;
import com.vuongle.imagine.services.core.auth.command.UpdateUserCommand;
import com.vuongle.imagine.services.core.auth.impl.UserServiceImpl;
import com.vuongle.imagine.services.share.auth.impl.UserQueryServiceImpl;
import com.vuongle.imagine.services.share.auth.query.UserQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@Tag(
        name = "ADMIN - user",
        description = "CRUD REST APIs for admin manage user"
)
public class AdminUserController {

    private final UserServiceImpl userService;

    private final UserQueryServiceImpl userQueryService;

    public AdminUserController(
            UserServiceImpl userService,
            UserQueryServiceImpl userQueryService
    ) {
        this.userService = userService;
        this.userQueryService = userQueryService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @Operation(
            summary = "Find user by some condition"
    )
    public ResponseEntity<Page<UserProfile>> adminFindAllUser(
            HttpServletRequest request,
            UserQuery userQuery,
            Pageable pageable
    ) {
        Page<UserProfile> users = userQueryService.findPage(userQuery, pageable, UserProfile.class);

        return ResponseEntity.ok(users);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<UserProfile> adminCreateUser(
            HttpServletRequest request,
            @RequestBody @Valid RegisterCommand registerCommand
    ) {

        UserProfile userProfile = userService.create(registerCommand);

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<UserProfile> adminUpdateUser(
            HttpServletRequest request,
            @PathVariable("id") ObjectId id,
            @RequestBody @Valid UpdateUserCommand updateUserCommand
    ) {
        updateUserCommand.setId(id);
        UserProfile userProfile = userService.update(updateUserCommand);

        return ResponseEntity.ok(userProfile);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Object> adminDeleteUser(
            HttpServletRequest request,
            @PathVariable("id") ObjectId id,
            @RequestParam("delete-forever") boolean deleteForever
    ) {
        userService.delete(id, deleteForever);

        return ResponseEntity.ok().build();
    }
}
