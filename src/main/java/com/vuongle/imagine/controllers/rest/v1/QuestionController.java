package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.quiz.QuestionResponse;
import com.vuongle.imagine.services.core.quiz.QuestionService;
import com.vuongle.imagine.services.share.quiz.QuestionQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuestionQuery;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@Tag(
        name = "Question",
        description = "CRUD REST APIs for question"
)
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
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Page<QuestionResponse>> findAllQuestion(
            HttpServletRequest request,
            QuestionQuery questionQuery,
            Pageable pageable
    ) {
        Page<QuestionResponse> quizPage = questionQueryService.findPage(questionQuery, pageable, QuestionResponse.class);
        return ResponseEntity.ok(quizPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<QuestionResponse> getDetail(
            @PathVariable(value = "id") ObjectId id
    ) {
        return ResponseEntity.ok(questionQueryService.getById(id, QuestionResponse.class));
    }
}
