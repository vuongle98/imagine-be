package com.vuongle.imagine.services.share.quiz.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayingQuizHistoryQuery implements Serializable {
    private ObjectId userId;

    private ObjectId quizId;

    private Integer fromNumOfCorrect;

    private Integer toNumOfCorrect;
}
