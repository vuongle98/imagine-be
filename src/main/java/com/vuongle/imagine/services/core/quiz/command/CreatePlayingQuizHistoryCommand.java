package com.vuongle.imagine.services.core.quiz.command;

import com.vuongle.imagine.dto.quiz.QuizResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayingQuizHistoryCommand implements Serializable {
    private ObjectId userId;

    private ObjectId quizId;

    private QuizResult result;
}
