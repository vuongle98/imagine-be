package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.Conversation;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ConversationRepository extends MongoRepository<Conversation, ObjectId> {

    @Query("{members: ?0}")
    Page<Conversation> findAllByMember(ObjectId memberId, Pageable pageable);
}
