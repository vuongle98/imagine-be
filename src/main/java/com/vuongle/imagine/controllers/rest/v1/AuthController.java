package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.auth.JwtResponse;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.services.core.auth.AuthService;
import com.vuongle.imagine.services.core.auth.command.LoginCommand;
import com.vuongle.imagine.services.core.auth.command.SignupCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Valid LoginCommand loginCommand
    ) {
        JwtResponse response = authService.login(loginCommand);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(
            @RequestBody @Valid SignupCommand signupCommand
    ) {
        User response = authService.signUp(signupCommand);
        return ResponseEntity.ok(response);
    }

}
