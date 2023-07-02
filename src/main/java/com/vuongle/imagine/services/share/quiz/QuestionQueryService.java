package com.vuongle.imagine.services.share.quiz;

import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.quiz.query.QuestionQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.List;
import java.util.Optional;

public interface QuestionQueryService extends BaseService<QuestionQuery> {
    Optional<Question> findQuestionById(ObjectId id);

    Question getQuestionById(ObjectId id);

    Page<Question> findPageQuestion(QuestionQuery questionQuery, Pageable pageable);

    Page<Question> findPageQuestion(QuestionQuery questionQuery, Pageable pageable, AggregationOperation... aggregationOperationInputs);

    List<Question> findListQuestion(QuestionQuery questionQuery);

    long countByCriteria(QuestionQuery questionQuery);

//    void checkAnswers(Question question, List<Answer> answers);
}
