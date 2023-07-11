package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.quiz.QuizResponse;
import com.vuongle.imagine.dto.quiz.QuizResult;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.services.core.quiz.QuizService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuizCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuizCommand;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/quiz")
@CrossOrigin(origins = "http://localhost:4200")
public class QuizController {

    private final QuizQueryService quizQueryService;

    private final QuizService quizService;

    public QuizController(
            QuizQueryService quizQueryService,
            QuizService quizService
    ) {
        this.quizService = quizService;
        this.quizQueryService = quizQueryService;
    }

    @GetMapping
    public ResponseEntity<Page<QuizResponse>> findPage(
            QuizQuery quizQuery,
            Pageable pageable
    ) throws InterruptedException {

        Thread.sleep(300);
        Page<QuizResponse> quizPage = quizQueryService.findPage(quizQuery, pageable, QuizResponse.class);

        return ResponseEntity.ok(quizPage);
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @Valid @RequestBody CreateQuizCommand command
    ) {
        Quiz quiz = quizService.createQuiz(command);

        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> getDetail(
            @PathVariable(value = "id") ObjectId id
    ) {
        return ResponseEntity.ok(quizQueryService.getById(id, QuizResponse.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid UpdateQuizCommand updateQuizCommand
    ) {
        updateQuizCommand.setId(id);
        Quiz quiz = quizService.updateQuiz(updateQuizCommand);

        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{id}/answer")
    public ResponseEntity<QuizResult> answerQuiz(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid List<UserCheckQuiz> checkQuiz
    ) {
        QuizResult quiz = quizQueryService.checkAnswer(id, checkQuiz);

        return ResponseEntity.ok(quiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(
            @PathVariable(value = "id") ObjectId id
    ) {
        quizService.delete(id);
        return ResponseEntity.ok(null);
    }
}
