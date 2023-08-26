package com.vuongle.imagine.services.core.auth.impl;

import com.vuongle.imagine.constants.UserRole;
import com.vuongle.imagine.dto.auth.JwtResponse;
import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.exceptions.DataFormatException;
import com.vuongle.imagine.exceptions.UserNotFoundException;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.repositories.UserRepository;
import com.vuongle.imagine.services.core.auth.AuthService;
import com.vuongle.imagine.services.core.auth.command.LoginCommand;
import com.vuongle.imagine.services.core.auth.command.RegisterCommand;
import com.vuongle.imagine.services.share.auth.impl.UserQueryServiceImpl;
import com.vuongle.imagine.utils.Context;
import com.vuongle.imagine.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final UserQueryServiceImpl userQueryService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse login(LoginCommand command) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(command.getUsername(), command.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();

        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .user(new UserProfile(userDetails))
                .build();

//        return new JwtResponse(
//                jwt,
//                "Bearer",
//                userDetails.getId(),
//                userDetails.getUsername(),
//                userDetails.getFullName(),
//                userDetails.getEmail(),
//                roles
//        );
    }

    @Override
    public User register(RegisterCommand command) {
        if (userRepository.existsByUsername(command.getUsername())) {
            throw new DataFormatException("Username already exists");
        }

        if (userRepository.existsByEmail(command.getEmail())) {
            throw new DataFormatException("Email already exists");
        }

        User user = new User(
                command.getUsername(),
                command.getFullName(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                List.of(UserRole.USER)
        );

        user = userRepository.save(user);
        return user;
    }

    @Override
    public UserProfile verifyUser() {
        User user = Context.getUser();

        if (Objects.isNull(user) || !user.isEnabled()) {
            throw new UserNotFoundException("User not found");
        }

        return userQueryService.findById(user.getId());
    }
}
