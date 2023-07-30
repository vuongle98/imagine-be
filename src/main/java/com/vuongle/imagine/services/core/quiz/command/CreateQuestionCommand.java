package com.vuongle.imagine.services.core.quiz.command;

import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.constants.QuestionType;
import com.vuongle.imagine.models.embeded.Answer;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
public class CreateQuestionCommand implements Serializable {

    private QuestionType type = QuestionType.YES_NO;

    @Nonnull
    @NotBlank
    private String title;

    private Boolean active = true;

    @Nonnull
    private List<Answer> answers;

    private Boolean mark = false;

    private Integer difficultlyLevel = 1;

    private QuizCategory category = QuizCategory.GENERAL;

    private Integer countDown = 30;

    public boolean validateCreateData() {
        return !answers.isEmpty();
    }
}
