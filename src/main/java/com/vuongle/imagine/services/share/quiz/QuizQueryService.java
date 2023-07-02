package com.vuongle.imagine.services.share.quiz;

import com.vuongle.imagine.dto.quiz.QuizResult;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuizQueryService extends BaseService<QuizQuery> {

    Quiz getById(ObjectId id);

    List<Quiz> findList(QuizQuery quizQuery);

    Page<Quiz> findPage(QuizQuery quizQuery, Pageable pageable);

    QuizResult checkAnswer(ObjectId quizId, List<UserCheckQuiz> answers);
}
