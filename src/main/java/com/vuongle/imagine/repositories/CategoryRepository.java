package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
}
