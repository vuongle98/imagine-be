package com.vuongle.imagine.services.share.quiz.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.dto.quiz.QuestionResponse;
import com.vuongle.imagine.dto.quiz.QuizResult;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.exceptions.UserNotFoundException;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.models.embeded.Answer;
import com.vuongle.imagine.repositories.QuizRepository;
import com.vuongle.imagine.services.core.quiz.PlayingQuizHistoryService;
import com.vuongle.imagine.services.core.quiz.command.CreatePlayingQuizHistoryCommand;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuizQuery;
import com.vuongle.imagine.utils.Context;
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
import java.util.stream.Collectors;

@Service
public class QuizQueryServiceImpl implements QuizQueryService {

    private final MongoTemplate mongoTemplate;

    private final QuizRepository quizRepository;

    private final PlayingQuizHistoryService playingQuizHistoryService;

    private final ObjectMapper objectMapper;

    public QuizQueryServiceImpl(
            QuizRepository quizRepository,
            MongoTemplate mongoTemplate,
            PlayingQuizHistoryService playingQuizHistoryService,
            ObjectMapper objectMapper
    ) {
        this.mongoTemplate = mongoTemplate;
        this.quizRepository = quizRepository;
        this.playingQuizHistoryService = playingQuizHistoryService;
        this.objectMapper = objectMapper;
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


        for (UserCheckQuiz checkQuiz : answers) {
            for (Question question : existedQuiz.getQuestions()) {
                if (question.getId().equals(checkQuiz.getQuestionId())) {
                    Integer correctNum = question.checkAnswer(checkQuiz);
                    totalCorrect += correctNum;
                    checkQuiz.setCorrectAnswerIds(question.getCorrectAnswer().stream().map(Answer::getId).collect(Collectors.toList()));
                    checkQuiz.setQuestion(objectMapper.convertValue(question, QuestionResponse.class));
                }
            }
        }

        result.setNumOfCorrectAnswers(totalCorrect);
        result.setTotalAnswers(answers.size());
        result.setAnswers(answers);

        User user = Context.getUser();

        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found");
        }

        // save history
        CreatePlayingQuizHistoryCommand command = new CreatePlayingQuizHistoryCommand();
        command.setQuizId(quizId);
        command.setUserId(user.getId());
        command.setResult(result);
        playingQuizHistoryService.createPlayingQuizHistory(command);

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
    public long countByQuery(QuizQuery query, AggregationOperation... aggregationOperationInputs) {
        return 0;
    }

    @Override
    public Criteria createCriteria(QuizQuery query) {

        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

//        if (Objects.nonNull(query.getLikeQuestion())) {
//
//        }

        if (Objects.nonNull(query.getLikeTitle())) {
            listAndCriteria.add(Criteria.where("title").regex(query.getLikeTitle(), "i"));
        }

        if (Objects.nonNull(query.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(query.getId()));
        }

        if (Objects.nonNull(query.getPublished())) {
            listAndCriteria.add(Criteria.where("published").is(query.getPublished()));
        }

        if (Objects.nonNull(query.getLikeDescription())) {
            listAndCriteria.add(Criteria.where("description").regex(query.getLikeDescription(), "i"));
        }

        if (Objects.nonNull(query.getCreatedBy())) {
            listAndCriteria.add(Criteria.where("createdBy").is(query.getCreatedBy()));
        }

        if (Objects.nonNull(query.getCategory())) {
            listAndCriteria.add(Criteria.where("category").is(query.getCategory()));
        }

        if (Objects.nonNull(query.getLevel())) {
            listAndCriteria.add(Criteria.where("level").is(query.getLevel()));
        }

        if (Objects.nonNull(query.getMark())) {
            listAndCriteria.add(Criteria.where("mark").is(query.getMark()));
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }
}
