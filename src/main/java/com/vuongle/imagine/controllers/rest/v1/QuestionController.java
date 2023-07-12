package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.quiz.QuestionResponse;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.services.core.quiz.QuestionService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuestionCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuestionCommand;
import com.vuongle.imagine.services.share.quiz.QuestionQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuestionQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@CrossOrigin(origins = "http://localhost:4200")
public class QuestionController {

    private final QuestionQueryService questionQueryService;

    private final QuestionService questionService;

    public QuestionController(
            QuestionService questionService,
            QuestionQueryService questionQueryService
    ) {
        this.questionService = questionService;
        this.questionQueryService = questionQueryService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<Page<QuestionResponse>> findAllQuestion(
            HttpServletRequest request,
            QuestionQuery questionQuery,
            Pageable pageable
    ) {
        Page<QuestionResponse> quizPage = questionQueryService.findPage(questionQuery, pageable, QuestionResponse.class);
        return ResponseEntity.ok(quizPage);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<Question> createQuestion(
            HttpServletRequest request,
            @RequestBody @Valid CreateQuestionCommand createCommand
    ) {
        Question question = questionService.createQuestion(createCommand);

        return ResponseEntity.ok(question);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<QuestionResponse> getDetail(
            @PathVariable(value = "id") ObjectId id
    ) {
        return ResponseEntity.ok(questionQueryService.getById(id, QuestionResponse.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody @Valid UpdateQuestionCommand updateQuizCommand
    ) {
        updateQuizCommand.setId(id);
        Question question = questionService.updateQuestion(updateQuizCommand);

        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable(value = "id") ObjectId id
    ) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(null);
    }
}
