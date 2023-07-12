package com.vuongle.imagine.config;

import com.vuongle.imagine.models.User;
import com.vuongle.imagine.utils.Context;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class UserAuditing implements AuditorAware<String> {

    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        User user = Context.getUser();
        if (Objects.nonNull(user))
            return Optional.of(user.getUsername());

        return Optional.empty();
    }
}
