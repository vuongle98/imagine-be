package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findAllByConversationIdOrderByTimeStampAsc(ObjectId conversationId);

}
