package com.vuongle.imagine.services.share.auth.query;

import com.vuongle.imagine.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery implements Serializable {
    private String username;
    private String likeUsername;
    private String likeFullName;

    private String likeEmail;
    private List<ObjectId> friendIds;
    private ObjectId id;
    private UserRole role;
}
