package com.vuongle.imagine.services.core.auth.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCommand implements Serializable {

    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    private String username;
    @Size(min = 2, max = 30, message = "FullName must be between 2 and 30 characters")
    private String fullName;

    private String avtUrl;
}
