package com.vuongle.imagine.services.core.auth.command;

import com.vuongle.imagine.constants.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterCommand extends LoginCommand implements Serializable {

    @NotNull(message = "Email must be not null")
    private String email;
    @NotNull(message = "Fullname must be not null")
    private String fullName;

    private List<UserRole> roles;
}
