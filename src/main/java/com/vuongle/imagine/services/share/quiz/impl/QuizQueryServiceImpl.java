package com.vuongle.imagine.services.share.quiz.impl;

import com.vuongle.imagine.dto.quiz.QuizResult;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.repositories.QuizRepository;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizQueryServiceImpl implements QuizQueryService {

    private final MongoTemplate mongoTemplate;

    private final QuizRepository quizRepository;

    public QuizQueryServiceImpl(
            QuizRepository quizRepository,
            MongoTemplate mongoTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
        this.quizRepository = quizRepository;
    }

    @Override
    public <T> Optional<T> findById(ObjectId id, Class<T> returnType) {
        Criteria criteria = Criteria.where("_id").is(id);
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.add(Aggregation.lookup(
                "question",
                "listQuestionId",
                "_id",
                "questions"
        ));
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        T result = mongoTemplate.aggregate(aggregation, returnType, returnType).getUniqueMappedResult();

        return Optional.ofNullable(result);
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> classType) {
        return findById(id, classType).orElseThrow(() -> new DataNotFoundException("Not found data has id " + id));
    }

    @Override
    public <T> Page<T> findPage(QuizQuery quizQuery, Pageable pageable, Class<T> returnType) {
        Query query = createQuery(quizQuery, pageable);

        List<T> listQuiz = mongoTemplate.find(query, returnType);

        return PageableExecutionUtils.getPage(
                listQuiz,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(QuizQuery quizQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        Criteria criteria = createCriteria(quizQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            aggregationOperations.add(Aggregation.sort(pageable.getSort()));
        }

        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.add(Aggregation.lookup(
                "question",
                "listQuestionId",
                "_id",
                "questions"
        ));
        aggregationOperations.addAll(Arrays.stream(aggregationOperationInputs).toList());
        aggregationOperations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        List<T> results = mongoTemplate.aggregate(aggregation, returnType, returnType).getMappedResults();
        return new PageImpl<>(results, pageable, countByQuery(quizQuery));
    }

    @Override
    public Quiz getById(ObjectId id) {
        return getById(id, Quiz.class);
    }

    @Override
    public List<Quiz> findList(QuizQuery quizQuery) {
        return findList(quizQuery, Quiz.class);
    }

    @Override
    public Page<Quiz> findPage(QuizQuery quizQuery, Pageable pageable) {
        return findPage(quizQuery, pageable, Quiz.class);
    }

    @Override
    public QuizResult checkAnswer(ObjectId quizId, List<UserCheckQuiz> answers) {

        QuizResult result = new QuizResult();
        Quiz existedQuiz = getById(quizId);

        Integer totalCorrect = 0;

        for (Question question: existedQuiz.getQuestions()) {

            for (UserCheckQuiz checkQuiz: answers) {
                if (question.getId().equals(checkQuiz.getQuestionId())) {
                    Integer correctNum = question.checkAnswer(checkQuiz);
                    totalCorrect += correctNum;
                }
            }
        }

        result.setCorrectAnswers(totalCorrect);
        result.setTotalAnswers(answers.size());
        result.setAnswers(answers);

        return result;
    }

    @Override
    public <T> List<T> findList(QuizQuery classType, Class<T> returnType) {
        Query query = createQuery(classType);
        return mongoTemplate.find(query, returnType);
    }

    @Override
    public Query createQuery(QuizQuery param, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(createCriteria(param));

        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }
        return query;
    }

    @Override
    public Query createQuery(QuizQuery query) {
        return createQuery(query, null);
    }

    @Override
    public long countByQuery(QuizQuery param) {
        Query query = createQuery(param);
        return mongoTemplate.count(query, Quiz.class);
    }

    @Override
    public Criteria createCriteria(QuizQuery query) {

        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(query.getLikeQuestion())) {

        }

        if (Objects.nonNull(query.getLikeTitle())) {
            listAndCriteria.add(Criteria.where("title").regex(query.getLikeTitle(), "i"));
        }

        if (Objects.nonNull(query.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(query.getId()));
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }
}
