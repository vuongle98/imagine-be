package com.vuongle.imagine.services.core.quiz;

import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.services.core.quiz.command.CreateQuizCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuizCommand;
import org.bson.types.ObjectId;

public interface QuizService {

    Quiz createQuiz(CreateQuizCommand command);

    Quiz updateQuiz(UpdateQuizCommand command);

    void delete(ObjectId id);
}
