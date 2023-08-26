package com.vuongle.imagine.services.share.auth.query;

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
    private String fullName;

    private String email;
    private List<ObjectId> friendIds;
    private ObjectId id;
}
