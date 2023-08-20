package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.auth.JwtResponse;
import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.services.core.auth.AuthService;
import com.vuongle.imagine.services.core.auth.command.LoginCommand;
import com.vuongle.imagine.services.core.auth.command.RegisterCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Valid LoginCommand loginCommand
    ) {
        JwtResponse response = authService.login(loginCommand);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> signUp(
            @RequestBody @Valid RegisterCommand registerCommand
    ) {
        User response = authService.register(registerCommand);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<UserProfile> verify() {
        UserProfile response = authService.verifyUser();
        return ResponseEntity.ok(response);
    }

}
