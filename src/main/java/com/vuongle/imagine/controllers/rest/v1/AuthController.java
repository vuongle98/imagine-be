package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.auth.JwtResponse;
import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.services.core.auth.AuthService;
import com.vuongle.imagine.services.core.auth.command.LoginCommand;
import com.vuongle.imagine.services.core.auth.command.RegisterCommand;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(
        name = "Auth",
        description = "CRUD REST APIs for authentication"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Valid LoginCommand loginCommand
    ) throws InterruptedException {
        Thread.sleep(1000);
        JwtResponse response = authService.login(loginCommand);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<User> signUp(
            @RequestBody @Valid RegisterCommand registerCommand
    ) {
        User response = authService.register(registerCommand);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<UserProfile> verify() {
        UserProfile response = authService.verifyUser();

        for (UserProfile friend : response.getFriends()) {
            response.getFriendship().stream()
                    .filter(f -> f.getId().equals(friend.getId()))
                    .forEach(f -> friend.setFriendStatus(f.getStatus()));
        }
        return ResponseEntity.ok(response);
    }

}
