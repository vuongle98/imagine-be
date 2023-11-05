package com.vuongle.imagine.models.embeded;

import com.vuongle.imagine.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Creator implements Serializable{

    @Id
    private ObjectId id;

    private String fullName;

    private Set<UserRole> roles;
}
