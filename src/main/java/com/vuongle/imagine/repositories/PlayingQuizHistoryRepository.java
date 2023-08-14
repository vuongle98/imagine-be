package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.PlayingQuizHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayingQuizHistoryRepository extends MongoRepository<PlayingQuizHistory, ObjectId> {
}
