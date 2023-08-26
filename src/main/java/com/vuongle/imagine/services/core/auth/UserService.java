package com.vuongle.imagine.services.core.auth;

import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.models.embeded.FriendData;
import com.vuongle.imagine.services.core.BaseService;
import com.vuongle.imagine.services.core.auth.command.RegisterCommand;
import com.vuongle.imagine.services.core.auth.command.UpdateUserCommand;
import org.bson.types.ObjectId;

public interface UserService extends BaseService<RegisterCommand, UpdateUserCommand, User> {

    UserProfile addFriend(ObjectId friendId);
}
