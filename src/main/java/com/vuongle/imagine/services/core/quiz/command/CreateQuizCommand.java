package com.vuongle.imagine.services.core.quiz.command;

import com.vuongle.imagine.constants.QuizType;
import com.vuongle.imagine.models.embeded.Answer;
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
public class CreateQuizCommand implements Serializable {

    private QuizType type = QuizType.YES_NO;

    private String question;

    private boolean isActive = true;

    private List<Answer> answers;

    private boolean mark;

    private Integer difficultlyLevel = 1;

    private Integer numOfCorrectAnswer = 1;

    public boolean validateCreateData() {
        return question != null && answers != null && !answers.isEmpty();
    }
}
