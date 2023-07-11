package com.vuongle.imagine.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse implements Serializable {
    private String token;
    private String type = "Bearer";
    private ObjectId id;
    private String username;
    private String email;
    private List<String> roles;

}
