package com.vuongle.imagine.services.core.quiz;

import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.services.core.quiz.command.CreateQuestionCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuestionCommand;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;

public interface QuestionService {
    Question createQuestion(CreateQuestionCommand quizCommand);

    Question updateQuestion(UpdateQuestionCommand quizCommand);

    void deleteQuestion(ObjectId id);
}
