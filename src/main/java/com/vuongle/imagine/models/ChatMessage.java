package com.vuongle.imagine.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Document("message")
public class ChatMessage {

    @Id
    private ObjectId id;

    private ObjectId conversationId;
    private String content;
    private ObjectId senderId;
    private Instant timeStamp;
    private ObjectId replyTo;
    private ObjectId fileId;

    private boolean deleted;
}
