package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.File;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, ObjectId> {
}
