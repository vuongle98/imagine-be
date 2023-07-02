package com.vuongle.imagine.services.core.quiz.command;


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

    private boolean isPublished = false;

    public boolean isValidData() {
        return Objects.nonNull(listQuestionId) && Objects.nonNull(title);
    }

}
