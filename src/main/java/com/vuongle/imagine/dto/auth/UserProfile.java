package com.vuongle.imagine.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vuongle.imagine.constants.FriendStatus;
import com.vuongle.imagine.constants.UserRole;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.models.embeded.FriendShipData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class UserProfile extends BaseUser implements Serializable {

    private List<UserProfile> friends;

    @JsonIgnore
    private List<FriendShipData> friendship;

    private FriendStatus friendStatus;

    private boolean locked;

    private boolean enabled;

    public UserProfile(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.avatarId = user.getAvatarId();
    }

}
