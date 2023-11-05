package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.services.core.quiz.QuestionService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuestionCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuestionCommand;
import com.vuongle.imagine.services.share.quiz.QuestionQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuestionQuery;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/question")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@Tag(
        name = "ADMIN - question",
        description = "CRUD REST APIs for admin manage question"
)
public class AdminQuestionController {

    private final QuestionQueryService questionQueryService;

    private final QuestionService questionService;

    public AdminQuestionController(
            QuestionService questionService,
            QuestionQueryService questionQueryService
    ) {
        this.questionService = questionService;
        this.questionQueryService = questionQueryService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Page<Question>> adminFindAllQuestion(
            HttpServletRequest request,
            QuestionQuery questionQuery,
            Pageable pageable
    ) throws InterruptedException{
        Thread.sleep(300);
        Page<Question> quizPage = questionQueryService.findPage(questionQuery, pageable, Question.class);
        return ResponseEntity.ok(quizPage);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Question> createQuestion(
            HttpServletRequest request,
            @RequestBody @Valid CreateQuestionCommand createCommand
    ) {
        Question question = questionService.createQuestion(createCommand);

        return ResponseEntity.ok(question);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Question> getDetail(
            @PathVariable(value = "id") ObjectId id
    ) throws InterruptedException {
        Thread.sleep(300);
        return ResponseEntity.ok(questionQueryService.getById(id, Question.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
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
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable(value = "id") ObjectId id
    ) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(null);
    }
}
