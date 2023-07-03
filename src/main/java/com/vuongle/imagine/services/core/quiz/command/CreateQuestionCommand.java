package com.vuongle.imagine.services.core.quiz.command;

import com.vuongle.imagine.constants.QuestionCategory;
import com.vuongle.imagine.constants.QuestionType;
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
public class CreateQuestionCommand implements Serializable {

    private QuestionType type = QuestionType.YES_NO;

    private String title;

    private boolean isActive = true;

    private List<Answer> answers;

    private boolean mark;

    private Integer difficultlyLevel = 1;

    private QuestionCategory category;

    public boolean validateCreateData() {
        return title != null && answers != null && !answers.isEmpty();
    }
}
