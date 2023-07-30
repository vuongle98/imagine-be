package com.vuongle.imagine.services.core.quiz.command;


import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.constants.QuizLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
public class CreateQuizCommand implements Serializable {

    @NotNull(message = "Not null list question id")
    private List<ObjectId> listQuestionId;

    @Size(min = 2, max = 100, message = "The title must be between 2 and 100 messages.")
    private String title;

    @Size(max = 500, message = "The description can't be longer than 500 characters.")
    private String description;

    private boolean published;

    private Integer countDown = 30;

    private QuizCategory category = QuizCategory.GENERAL;

    private QuizLevel level = QuizLevel.EASY;

    private boolean mark;

    public boolean isValidateData() {
        return !listQuestionId.isEmpty() && Objects.nonNull(title) && !title.isEmpty();
    }
}
