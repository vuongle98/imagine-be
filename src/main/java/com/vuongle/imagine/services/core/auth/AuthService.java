package com.vuongle.imagine.services.core.auth;

import com.vuongle.imagine.dto.auth.JwtResponse;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.services.core.auth.command.LoginCommand;
import com.vuongle.imagine.services.core.auth.command.SignupCommand;

public interface AuthService {

    JwtResponse login(LoginCommand command);

    User signUp(SignupCommand command);

}
