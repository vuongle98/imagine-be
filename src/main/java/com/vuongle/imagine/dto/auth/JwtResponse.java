package com.vuongle.imagine.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtResponse implements Serializable {
    private String token;
    private String type = "Bearer";

    private UserProfile user;
}
