package com.vuongle.imagine.models.embeded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseAnswer implements Serializable {
    private String answer;

    @Id
    private ObjectId id = new ObjectId();
}
