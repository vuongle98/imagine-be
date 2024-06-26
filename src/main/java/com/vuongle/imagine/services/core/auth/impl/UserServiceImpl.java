package com.vuongle.imagine.services.core.auth.impl;

import com.vuongle.imagine.constants.FriendStatus;
import com.vuongle.imagine.constants.UserRole;
import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.exceptions.UnAuthorizationException;
import com.vuongle.imagine.exceptions.UserNotFoundException;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.models.embeded.FriendShipData;
import com.vuongle.imagine.repositories.UserRepository;
import com.vuongle.imagine.services.core.auth.UserService;
import com.vuongle.imagine.services.core.auth.command.RegisterCommand;
import com.vuongle.imagine.services.core.auth.command.UpdateUserCommand;
import com.vuongle.imagine.services.share.auth.impl.UserQueryServiceImpl;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserQueryServiceImpl userQueryService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserQueryServiceImpl userQueryService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userQueryService = userQueryService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserProfile addFriend(ObjectId friendId) {
        return changeFriendShipStatus(friendId, FriendStatus.REQUESTED);
    }

    @Override
    public UserProfile acceptFriend(ObjectId friendId) {
        return changeFriendShipStatus(friendId, FriendStatus.ACCEPTED);
    }

    @Override
    public UserProfile declineFriend(ObjectId friendId) {
        return changeFriendShipStatus(friendId, FriendStatus.REJECTED);
    }

    @Override
    public UserProfile removeFriend(ObjectId friendId) {
        return changeFriendShipStatus(friendId, FriendStatus.REMOVE);
    }

    @Override
    public UserProfile setUserOnline(ObjectId userId, boolean online) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not found user"));

        user.setOnline(online);

        if (!online) {
            user.setLastActive(Instant.now());
        }

        user = userRepository.save(user);

        return userQueryService.findById(user.getId());
    }

    private UserProfile changeFriendShipStatus(ObjectId friendId, FriendStatus status) {

        User currentUser = Context.getUser();

        if (Objects.isNull(currentUser)) {
            throw new UserNotFoundException("Not found user");
        }

        FriendShipData friendShipData = new FriendShipData();
        friendShipData.setId(friendId);
        friendShipData.setStatus(status);

        UpdateUserCommand command = new UpdateUserCommand();
        command.setId(currentUser.getId());
        command.setFriendShipData(List.of(friendShipData));

        return update(command);
    }

    @Override
    public UserProfile create(RegisterCommand command) {

        User currentUser = Context.getUser();

        if (Objects.isNull(currentUser)) {
            return null;
        }

        List<UserRole> roles = List.of(UserRole.USER);

        // nếu admin mới dc thêm role, còn lại chỉ role user
        if (currentUser.getRoles().contains(UserRole.ADMIN)) {
            // init user role
            roles = command.getRoles();
        }

        User user = new User(
                command.getUsername(),
                command.getFullName(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                roles
        );

        user = userRepository.save(user);

        return userQueryService.getById(user.getId(), UserProfile.class);
    }

    @Override
    public UserProfile update(UpdateUserCommand command) {

        // find existing user
        User user = userQueryService.getById(command.getId(), User.class);
        if (Objects.isNull(user)) {
            return null;
        }

        User currentUser = Context.getUser();

        if (Objects.isNull(currentUser)) {
            return null;
        }

        // nếu user hiện tại có quyền admin mới được cập nhật username + roles
        if (currentUser.getRoles().contains(UserRole.ADMIN) || currentUser.getRoles().contains(UserRole.MODERATOR)) {
            if (Objects.nonNull(command.getRoles())) {

                List<UserRole> roles = command.getRoles();

                // nếu không có quyền admin thì k đc phân quyền admin
                if (currentUser.getRoles().stream().noneMatch(role -> role.equals(UserRole.ADMIN))) {
                    roles.remove(UserRole.ADMIN);
                }
                user.setRoles(roles);
            }

            if (Objects.nonNull(command.getUsername())) {
                user.setUsername(command.getUsername());
            }
        }

        if (Objects.nonNull(command.getFullName())) {
            user.setFullName(command.getFullName());
        }

        if (Objects.nonNull(command.getEmail())) {
            user.setEmail(command.getEmail());
        }

        if (Objects.nonNull(command.getPassword())) {
            user.setPassword(passwordEncoder.encode(command.getPassword()));
        }

        if (Objects.nonNull(command.getEnabled())) {
            user.setEnabled(command.getEnabled());
        }

        if (Objects.nonNull(command.getLocked())) {
            user.setLocked(command.getLocked());
        }

        if (Objects.nonNull(command.getOnline())) {
            user.setOnline(command.getOnline());
            if (!command.getOnline()) {
                user.setLastActive(Instant.now());
            }
        }

        if (Objects.nonNull(command.getFriendShipData())) {

            for (FriendShipData friendShipData : command.getFriendShipData()) {
                switch (friendShipData.getStatus()) {
                    case REQUESTED -> addFriend(user, friendShipData.getId());
                    case ACCEPTED -> acceptFriend(user, friendShipData.getId());
                    case REJECTED -> declineFriend(user, friendShipData.getId());
                    case REMOVE -> removeFriend(user, friendShipData.getId());
                }
            }
        }

        user = userRepository.save(user);

        return userQueryService.getById(user.getId(), UserProfile.class);
    }

    @Override
    public void delete(ObjectId id, boolean delete) {
        User user = Context.getUser();

        if (Objects.isNull(user)) {
            throw new UnAuthorizationException("No permission");
        }

        if (!user.getRoles().contains(UserRole.ADMIN) && !user.getRoles().contains(UserRole.MODERATOR)) {
            throw new UnAuthorizationException("No permission");
        }

        if (delete) {
            userRepository.deleteById(id);
        } else {
            // find user
            User currentUser = userQueryService.getById(id, User.class);
            if (Objects.isNull(currentUser)) {
                throw new UserNotFoundException("User not found");
            }
            // set delete status
            currentUser.setLocked(true);

            userRepository.save(currentUser);
        }

    }

    private void addFriend(User user, ObjectId friendId) {

        // check friendId exists
        if (!userRepository.existsById(friendId)) {
            throw new DataNotFoundException("Friend not found");
        }

        // current user is requested
        user.addFriend(friendId);

        // friend is pending
        User friend = userQueryService.getById(friendId, User.class);
        friend.pendingFriend(user.getId());
        userRepository.save(friend);
    }

    private void acceptFriend(User user, ObjectId friendId) {
        // no need to check if friendId exists because it is checked in addFriend
        // current user accept friend request
        user.acceptFriend(friendId);

        // friend is accepted
        User friend = userQueryService.getById(friendId, User.class);
        friend.acceptFriend(user.getId());
        userRepository.save(friend);
    }

    private void declineFriend(User user, ObjectId friendId) {

        // no need to check if friendId exists because it is checked in addFriend
        // current user decline friend request
        user.declineFriend(friendId);

        // friend is declined
        User friend = userQueryService.getById(friendId, User.class);
        friend.declineFriend(user.getId());
        userRepository.save(friend);
    }

    private void removeFriend(User user, ObjectId friendId) {
        // no need to check if friendId exists because it is checked in addFriend
        // current user remove friend
        user.removeFriend(friendId);

        User friend = userQueryService.getById(friendId, User.class);
        friend.removeFriend(user.getId());
        userRepository.save(friend);
    }
}
