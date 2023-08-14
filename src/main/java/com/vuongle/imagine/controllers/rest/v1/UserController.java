package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.models.PlayingQuizHistory;
import com.vuongle.imagine.services.core.auth.UserService;
import com.vuongle.imagine.services.share.quiz.PlayingQuizHistoryQueryService;
import com.vuongle.imagine.services.share.quiz.query.PlayingQuizHistoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {


    private final PlayingQuizHistoryQueryService playingQuizHistoryQueryService;

    public UserController(
            PlayingQuizHistoryQueryService playingQuizHistoryQueryService
    ) {
        this.playingQuizHistoryQueryService = playingQuizHistoryQueryService;
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

}
