package com.vuongle.imagine.services.share.quiz.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuestionQuery {
    private String likeQuestion;

    private ObjectId id;

    private boolean active = true;

    private boolean mark = false;

    private String likeAnswer;

    private Integer difficultlyLevel;

    private Integer numOfCorrectAnswer;

}
