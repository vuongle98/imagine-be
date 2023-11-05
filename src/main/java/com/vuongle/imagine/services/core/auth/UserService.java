package com.vuongle.imagine.services.core.auth;

import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.services.core.BaseService;
import com.vuongle.imagine.services.core.auth.command.RegisterCommand;
import com.vuongle.imagine.services.core.auth.command.UpdateUserCommand;
import org.bson.types.ObjectId;

public interface UserService extends BaseService<RegisterCommand, UpdateUserCommand, UserProfile> {

    UserProfile addFriend(ObjectId friendId);

    UserProfile acceptFriend(ObjectId friendId);

    UserProfile declineFriend(ObjectId friendId);

    UserProfile removeFriend(ObjectId friendId);

    UserProfile setUserOnline(ObjectId userId, boolean online);
}
