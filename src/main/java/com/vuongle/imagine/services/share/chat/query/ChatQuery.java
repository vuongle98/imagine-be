package com.vuongle.imagine.services.share.chat.query;

import com.vuongle.imagine.constants.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatQuery implements Serializable {

    private ObjectId id;
    private ObjectId conversationId;
    private String likeContent;
    private ObjectId senderId;
    private ChatType type;

    private Instant from;
    private Instant to;
}
