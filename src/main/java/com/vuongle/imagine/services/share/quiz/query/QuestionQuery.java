package com.vuongle.imagine.services.share.quiz.query;

import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.constants.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuestionQuery {
    private String likeTitle;

    private ObjectId id;

    private List<ObjectId> listId;

    private Boolean active = true;

    private Boolean mark = false;

    private String likeAnswer;

    private Integer difficultlyLevel;

    private Integer numOfCorrectAnswer;
    private QuizCategory category;

    private QuestionType type;

    private String createdBy;

}
