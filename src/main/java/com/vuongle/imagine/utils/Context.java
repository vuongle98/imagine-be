package com.vuongle.imagine.utils;

import com.vuongle.imagine.models.User;
import com.vuongle.imagine.services.core.auth.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class Context {

    public static User getUser() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return null;
        }

        Authentication context = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetail = (UserDetailsImpl) context.getPrincipal();

        return new User(userDetail);
    }
}
