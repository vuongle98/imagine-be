package com.vuongle.imagine.services.share.quiz;

import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.List;
import java.util.Optional;

public interface QuizQueryService {
    Optional<Quiz> findQuizById(ObjectId id);

    Quiz getQuizById(ObjectId id);

    Page<Quiz> findPageQuiz(QuizQuery quizQuery, Pageable pageable);

    Page<Quiz> findPageQuiz(QuizQuery quizQuery, Pageable pageable, AggregationOperation ...aggregationOperationInputs);

    List<Quiz> findListQuiz(QuizQuery quizQuery);

    long countByCriteria(QuizQuery quizQuery);
}
