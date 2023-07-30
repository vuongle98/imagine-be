package com.vuongle.imagine.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtResponse implements Serializable {
    private String token;
    private String type = "Bearer";
    private ObjectId id;
    private String username;
    private String fullName;
    private String email;
    private List<String> roles;
}
