package com.vuongle.imagine.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("conversation")
public class Conversation {

    @Id
    private ObjectId id;

    private String name;

    private List<ObjectId> members;

    private boolean deleted;

    private Instant timeStamp;
}
