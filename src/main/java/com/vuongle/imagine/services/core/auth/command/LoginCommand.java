package com.vuongle.imagine.services.core.auth.command;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCommand implements Serializable {
//    @NotNull(message = "Username must be not null")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    private String username;
//    @NotNull(message = "Password must be not null")
    private String password;
}
