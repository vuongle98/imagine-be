package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {

    void deleteAllByPostId(ObjectId postId);

    void deleteAllByCategoryId(ObjectId categoryId);
}
