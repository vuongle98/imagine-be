package com.vuongle.imagine.models;

import com.vuongle.imagine.constants.QuestionType;
import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.models.embeded.Answer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Document("question")
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Question implements Serializable {
    @Id
    @NotNull
    private ObjectId id;

    @NotNull
    private QuestionType type = QuestionType.YES_NO;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private String codeDescription;

    private File fileDescription;

    private ObjectId imageDescId;

    private boolean active = true;

    @NotNull
    private List<Answer> answers;

    private boolean mark;

    @NotNull
    private Integer difficultlyLevel = 1;

    @NotNull
    private List<Answer> correctAnswer;

    @NotNull
    private QuizCategory category = QuizCategory.GENERAL;

    @NotNull
    private Integer score = 0;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

    @NotNull
    private Integer countDown;

    @LastModifiedBy
    private String lastModifiedBy;

    public Integer checkAnswer(UserCheckQuiz checkQuiz) {
        int correct = 0;
        for (Answer answer : correctAnswer) {
            if (checkQuiz.getAnswerIds().stream().anyMatch(answerId -> answerId.equals(answer.getId()))) {
                correct += 1;
            }
        }

        return correct;
    }

}
