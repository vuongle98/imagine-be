package com.vuongle.imagine.models;

import com.vuongle.imagine.constants.ChatReadStatus;
import com.vuongle.imagine.constants.ChatType;
import com.vuongle.imagine.models.embeded.Sender;
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
    private Sender sender;
    private Instant timeStamp;
    private ObjectId replyTo;
    private ObjectId fileId;
    private ChatType type = ChatType.PRIVATE;
    private ChatReadStatus readStatus;

    private boolean deleted;
}
