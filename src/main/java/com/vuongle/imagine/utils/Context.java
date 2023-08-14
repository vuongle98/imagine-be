package com.vuongle.imagine.utils;

import com.vuongle.imagine.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class Context {

    public static User getUser() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return null;
        }

        Authentication context = SecurityContextHolder.getContext().getAuthentication();

        return (User)context.getPrincipal();
    }
}
