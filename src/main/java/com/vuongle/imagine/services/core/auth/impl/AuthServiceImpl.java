package com.vuongle.imagine.services.core.auth.impl;

import com.vuongle.imagine.dto.auth.JwtResponse;
import com.vuongle.imagine.dto.common.MessageResponse;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.repositories.UserRepository;
import com.vuongle.imagine.services.core.auth.AuthService;
import com.vuongle.imagine.services.core.auth.command.LoginCommand;
import com.vuongle.imagine.services.core.auth.command.SignupCommand;
import com.vuongle.imagine.services.core.auth.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtResponse login(LoginCommand command) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(command.getUsername(), command.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtResponse(
                jwt,
                "Bearer",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }

    @Override
    public User signUp(SignupCommand command) {
        if (userRepository.existsByUsername(command.getUsername())) {
            return null;
        }

        if (userRepository.existsByEmail(command.getEmail())) {
            return null;
        }

        User user = new User(
                command.getUsername(),
                command.getFullName(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                command.getRoles()
        );

        user = userRepository.save(user);
        return user;
    }
}
