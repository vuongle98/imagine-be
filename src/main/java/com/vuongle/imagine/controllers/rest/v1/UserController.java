package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.models.PlayingQuizHistory;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.repositories.UserRepository;
import com.vuongle.imagine.services.core.auth.UserService;
import com.vuongle.imagine.services.core.auth.impl.UserServiceImpl;
import com.vuongle.imagine.services.share.auth.impl.UserQueryServiceImpl;
import com.vuongle.imagine.services.share.auth.query.UserQuery;
import com.vuongle.imagine.services.share.quiz.PlayingQuizHistoryQueryService;
import com.vuongle.imagine.services.share.quiz.query.PlayingQuizHistoryQuery;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {


    private final PlayingQuizHistoryQueryService playingQuizHistoryQueryService;

    private final UserQueryServiceImpl userQueryService;

    private final UserRepository userRepository;

    private final UserServiceImpl userService;

    public UserController(
            PlayingQuizHistoryQueryService playingQuizHistoryQueryService,
            UserQueryServiceImpl userQueryService,
            UserRepository userRepository,
            UserServiceImpl userService
    ) {
        this.playingQuizHistoryQueryService = playingQuizHistoryQueryService;
        this.userQueryService = userQueryService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/playing-quiz-history")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<Page<PlayingQuizHistory>> getHistory(
            PlayingQuizHistoryQuery quizHistoryQuery,
            Pageable pageable
    ) {

        Page<PlayingQuizHistory> data = playingQuizHistoryQueryService.findByCurrentUser(quizHistoryQuery, pageable);

        return ResponseEntity.ok(data);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<UserProfile> getProfile(
            @RequestParam("username") String username
    ) {
        // get user
        User user = Context.getUser();

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        UserProfile userProfile = userQueryService.findById(user.getId());

        if (Objects.nonNull(username) && !userProfile.getUsername().equals(username)) {
            if (userProfile.getFriends().stream().anyMatch(friend -> friend.getUsername().equals(username))) {
                // check is friend
                UserProfile foundProfile = userQueryService.findByUsername(username);
                return ResponseEntity.ok(foundProfile);
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/add-friend")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<UserProfile> addFriend(
            @RequestParam("friend-id") ObjectId friendId
    ) {

        UserProfile userProfile = userService.addFriend(friendId);
        return ResponseEntity.ok(userProfile);
    }

}
