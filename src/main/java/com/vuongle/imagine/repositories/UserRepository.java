package com.vuongle.imagine.repositories;

import com.vuongle.imagine.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("{'friendId': {$in: ?0}}")
    List<User> findFriends(List<ObjectId> ids);

    @Query("{'username': {$in: ?0}}")
    boolean isFriend(ObjectId username, ObjectId friendUsername);
}
