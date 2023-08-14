package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.quiz.QuizResponse;
import com.vuongle.imagine.dto.quiz.QuizResult;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.models.PlayingQuizHistory;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.services.core.quiz.QuizService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuizCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuizCommand;
import com.vuongle.imagine.services.share.quiz.PlayingQuizHistoryQueryService;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.quiz.query.PlayingQuizHistoryQuery;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
public class QuizController {

    private final QuizQueryService quizQueryService;

    private final QuizService quizService;

    private final PlayingQuizHistoryQueryService playingQuizHistoryQueryService;

    public QuizController(
            QuizQueryService quizQueryService,
            QuizService quizService,
            PlayingQuizHistoryQueryService playingQuizHistoryQueryService
    ) {
        this.quizService = quizService;
        this.quizQueryService = quizQueryService;
        this.playingQuizHistoryQueryService = playingQuizHistoryQueryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<Page<QuizResponse>> findPage(
            QuizQuery quizQuery,
            Pageable pageable
    ) throws InterruptedException {

        Thread.sleep(300);
        Page<QuizResponse> quizPage = quizQueryService.findPage(quizQuery, pageable, QuizResponse.class);

        return ResponseEntity.ok(quizPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<QuizResponse> getDetail(
            @PathVariable(value = "id") ObjectId id
    ) {
        return ResponseEntity.ok(quizQueryService.getById(id, QuizResponse.class));
    }

    @PostMapping("/{id}/answer")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<QuizResult> answerQuiz(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid List<UserCheckQuiz> checkQuiz
    ) {
        QuizResult quiz = quizQueryService.checkAnswer(id, checkQuiz);

        return ResponseEntity.ok(quiz);
    }
}
