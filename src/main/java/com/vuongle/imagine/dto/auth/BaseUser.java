package com.vuongle.imagine.dto.auth;

import com.vuongle.imagine.constants.Gender;
import com.vuongle.imagine.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUser implements Serializable {
    @Id
    protected ObjectId id;

    protected String fullName;

    @NotEmpty(message = "Please provide your username")
    protected String username;

    protected Set<UserRole> roles = Set.of(UserRole.USER);

    @Email()
    protected String email;

    protected ObjectId avatarId;

    protected Instant birthDate;

    protected Gender gender = Gender.MALE;

    protected String address;
}
