package com.vuongle.imagine.models;

import com.vuongle.imagine.dto.quiz.QuizResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayingQuizHistory implements Serializable {

    private ObjectId userId;

    private ObjectId quizId;

    private QuizResult result;
}
