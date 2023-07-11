package com.vuongle.imagine.services.core.auth.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCommand implements Serializable {

    private String username;
    private String fullName;
    private String avtUrl;
}
