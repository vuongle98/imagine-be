package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findAllByConversationIdOrderByTimeStampAsc(ObjectId conversationId);

    List<ChatMessage> findAllByConversationIdOrderByIdDesc(ObjectId conversationId, Pageable pageable);

    @Query(value = "{type: 'PUBLIC'}", sort = "{_id: -1}")
    List<ChatMessage> findAllPublicMessages(Pageable pageable);
}
