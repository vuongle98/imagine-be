package com.vuongle.imagine.models;

import com.vuongle.imagine.constants.ChatReadStatus;
import com.vuongle.imagine.constants.ChatType;
import com.vuongle.imagine.models.embeded.Sender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Document("message")
public class ChatMessage {

    @Id
    private ObjectId id;

    @NotNull
    private ObjectId conversationId;
    @NotNull
    private String content;
    @NotNull
    private Sender sender;
    @NotNull
    private Instant timeStamp;
    private ObjectId replyTo;
    private ObjectId fileId;
    @NotNull
    private ChatType type = ChatType.PRIVATE;
    @NotNull
    private ChatReadStatus readStatus = ChatReadStatus.UNREAD;

    private boolean deleted;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;
}
