package com.vuongle.imagine.controllers;

import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.services.core.quiz.QuizService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuizCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuizCommand;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizQueryService quizQueryService;

    private final QuizService quizService;

    public QuizController(
            QuizService quizService,
            QuizQueryService quizQueryService
    ) {
        this.quizService = quizService;
        this.quizQueryService = quizQueryService;
    }

    @GetMapping()
    public ResponseEntity<Page<Quiz>> findAllQuiz(
            HttpServletRequest request,
            QuizQuery quizQuery,
            Pageable pageable
    ) {
        Page<Quiz> quizPage = quizQueryService.findPageQuiz(quizQuery, pageable);
        return ResponseEntity.ok(quizPage);
    }

    @PostMapping()
    public ResponseEntity<Quiz> createQuiz(
            HttpServletRequest request,
            @RequestBody CreateQuizCommand createCommand
    ) {
        Quiz quiz = quizService.createQuiz(createCommand);

        return ResponseEntity.ok(quiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody UpdateQuizCommand updateQuizCommand
    ) {
        updateQuizCommand.setId(id);
        Quiz quiz = quizService.updateQuiz(updateQuizCommand);

        return ResponseEntity.ok(quiz);
    }
}
