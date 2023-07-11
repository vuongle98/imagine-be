package com.vuongle.imagine.services.share.auth.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery implements Serializable {
    private String username;
    private String fullName;
    private ObjectId id;
}
