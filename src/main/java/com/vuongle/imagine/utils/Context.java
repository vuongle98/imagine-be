package com.vuongle.imagine.utils;

import com.vuongle.imagine.dto.auth.BaseUser;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.models.embeded.Creator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class Context {

    public static User getUser() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return null;
        }

        Authentication context = SecurityContextHolder.getContext().getAuthentication();

        if (!(context.getPrincipal() instanceof User)) {
            return null;
        }

        return (User) context.getPrincipal();
    }

    public static BaseUser getBaseUser() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return null;
        }

        Authentication context = SecurityContextHolder.getContext().getAuthentication();

        if (!(context.getPrincipal() instanceof User)) {
            return null;
        }

        return (BaseUser) context.getPrincipal();
    }

    public static Creator getCreator() {
        User user = getUser();

        if (user == null) return null;

        return new Creator(user.getId(), user.getFullName(), user.getRoles());
    }

    public static boolean hasModifyPermission() {
        User user = getUser();

        if (user == null) return false;

        return user.hasModifyPermission();
    }
}
