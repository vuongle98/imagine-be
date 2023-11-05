package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, ObjectId> {


    void deleteAllByCategoryId(ObjectId categoryId);

}
