package com.vuongle.imagine.models.embeded;

import com.vuongle.imagine.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sender implements Serializable {

    private ObjectId id;

    private String fullName;

    private String username;

    public Sender(String username, String fullName) {
        this.fullName = fullName;
        this.username = username;
    }

    public Sender(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
    }
}
