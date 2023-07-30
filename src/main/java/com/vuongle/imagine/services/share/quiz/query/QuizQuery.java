package com.vuongle.imagine.services.share.quiz.query;

import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.constants.QuizLevel;
import com.vuongle.imagine.services.share.quiz.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuizQuery extends BaseQuery implements Serializable {

    private ObjectId id;

    private String likeTitle;

    private String likeQuestion;

    private String likeDescription;

    private Boolean published;

    private String createdBy;

    private Integer countDownFrom;

    private Integer countDownTo;

    private QuizCategory category;

    private QuizLevel level;

    private Boolean mark;
}
