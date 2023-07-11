package com.vuongle.imagine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vuongle.imagine.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class User implements Serializable {

    @Id
    private ObjectId id;

    private String fullName;

    @NotEmpty(message = "Please provide your username")
    private String username;

    private boolean enable = true;

    @Email()
    private String email;

    @NotEmpty(message = "Please provide your password")
    @JsonIgnore
    private String password;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

    private Set<UserRole> roles;

    public User(
            String username,
            String fullName,
            String email,
            String password,
            List<UserRole> roles
    ) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>(roles);
    }
}
