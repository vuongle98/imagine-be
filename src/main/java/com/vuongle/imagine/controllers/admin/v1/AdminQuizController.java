package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.dto.quiz.QuizResponse;
import com.vuongle.imagine.dto.quiz.QuizResult;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.services.core.quiz.QuizService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuizCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuizCommand;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.quiz.impl.QuizQueryServiceImpl;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/quiz")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
public class AdminQuizController {

    private final QuizQueryServiceImpl quizQueryService;

    private final QuizService quizService;

    public AdminQuizController(
            QuizQueryServiceImpl quizQueryService,
            QuizService quizService
    ) {
        this.quizService = quizService;
        this.quizQueryService = quizQueryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<Page<Quiz>> findPage(
            QuizQuery quizQuery,
            Pageable pageable
    ) throws InterruptedException {

        Thread.sleep(300);
        AggregationOperation lookUp = Aggregation.lookup(
                "question",
                "listQuestionId",
                "_id",
                "questions"
        );

        Page<Quiz> quizPage = quizQueryService.findPage(quizQuery, pageable, Quiz.class, lookUp);

        return ResponseEntity.ok(quizPage);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<Quiz> createQuiz(
            @Valid @RequestBody CreateQuizCommand command
    ) {
        Quiz quiz = quizService.createQuiz(command);

        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<Quiz> getDetail(
            @PathVariable(value = "id") ObjectId id
    ) {
        return ResponseEntity.ok(quizQueryService.getById(id, Quiz.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<Quiz> updateQuiz(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid UpdateQuizCommand updateQuizCommand
    ) throws InterruptedException {
        Thread.sleep(300);
        updateQuizCommand.setId(id);
        Quiz quiz = quizService.updateQuiz(updateQuizCommand);

        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{id}/answer")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<QuizResult> answerQuiz(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid List<UserCheckQuiz> checkQuiz
    ) {
        QuizResult quiz = quizQueryService.checkAnswer(id, checkQuiz);

        return ResponseEntity.ok(quiz);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> deleteQuiz(
            @PathVariable(value = "id") ObjectId id
    ) {
        quizService.delete(id);
        return ResponseEntity.ok(null);
    }
}
