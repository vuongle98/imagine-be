package com.vuongle.imagine.dto.auth;

import com.vuongle.imagine.constants.UserRole;
import com.vuongle.imagine.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile extends BaseUser implements Serializable {

    private String test;

    public UserProfile(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.avatarId = user.getAvatarId();
    }

}
