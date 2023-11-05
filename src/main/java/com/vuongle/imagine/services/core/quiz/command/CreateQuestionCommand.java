package com.vuongle.imagine.services.core.quiz.command;

import com.vuongle.imagine.constants.QuestionType;
import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.exceptions.DataFormatException;
import com.vuongle.imagine.models.embeded.Answer;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
public class CreateQuestionCommand implements Serializable {

    private QuestionType type = QuestionType.YES_NO;

    @NotNull
    @NotBlank(message = "The title must not be blank")
    @Size(min = 2, max = 100, message = "The title must be between 2 and 100 messages")
    private String title;

    private Boolean active = true;

    @NotNull(message = "Not null list answer")
    @Size(min = 2, max = 4, message = "List answer must be between 2 and 4")
    private List<Answer> answers;

    private Boolean mark = false;

    private Integer difficultlyLevel = 1;

    private QuizCategory category = QuizCategory.GENERAL;

    @Min(value = 10, message = "The count down must be greater than 10")
    @Max(value = 60, message = "The count down must be less than 60")
    private Integer countDown = 30;

    public boolean validateQuestion() {

        if (Objects.equals(type, QuestionType.YES_NO) && answers.stream().filter(Answer::isCorrect).count() > 1){
            throw new DataFormatException("Câu hỏi yes/no chỉ có 1 đáp án đúng");
        }

        return true;
    }
}
