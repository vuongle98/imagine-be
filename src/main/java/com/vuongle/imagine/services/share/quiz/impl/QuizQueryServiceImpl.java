package com.vuongle.imagine.services.share.quiz.impl;

import com.vuongle.imagine.exceptions.DataNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class QuizQueryServiceImpl implements QuizQueryService {

    private final QuizRepository quizRepository;

    private final MongoTemplate mongoTemplate;

    public QuizQueryServiceImpl(
            QuizRepository quizRepository,
            MongoTemplate mongoTemplate
    ) {
        this.quizRepository = quizRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Quiz> findQuizById(ObjectId id) {
        return quizRepository.findById(id);
    }

    @Override
    public Quiz getQuizById(ObjectId id) {
        return findQuizById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy quiz " + id));
    }

    @Override
    public Page<Quiz> findPageQuiz(QuizQuery quizQuery, Pageable pageable) {
        Query query = createQuery(pageable, quizQuery);

        List<Quiz> listQuiz = mongoTemplate.find(query, Quiz.class);

        return PageableExecutionUtils.getPage(
                listQuiz,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Quiz.class)
        );
    }

    @Override
    public Page<Quiz> findPageQuiz(QuizQuery quizQuery, Pageable pageable, AggregationOperation... aggregationOperationInputs) {
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

        List<Quiz> results = mongoTemplate.aggregate(aggregation, Quiz.class, Quiz.class).getMappedResults();
        return new PageImpl<>(results, pageable, countByCriteria(quizQuery));

    }

    @Override
    public List<Quiz> findListQuiz(QuizQuery quizQuery) {
        Query query = createQuery(quizQuery);
        return mongoTemplate.find(query, Quiz.class);
    }

    @Override
    public long countByCriteria(QuizQuery quizQuery) {
        Query query = createQuery(quizQuery);
        return mongoTemplate.count(Query.of(query), Quiz.class);
    }


    private Query createQuery(Pageable pageable, QuizQuery quizQuery) {
        Criteria criteria = createCriteria(quizQuery);

        Query query = new Query();
        query.addCriteria(criteria);
        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }
        return query;
    }

    private Query createQuery(QuizQuery quizQuery) {
        return createQuery(null, quizQuery);
    }

    private Criteria createCriteria(QuizQuery quizQuery) {
        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(quizQuery.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(quizQuery.getId()));
        }

        if (Objects.nonNull(quizQuery.getLikeQuestion())) {
            listAndCriteria.add(Criteria.where("question").regex(quizQuery.getLikeQuestion(), "i"));
        }

        listAndCriteria.add(Criteria.where("active").is(quizQuery.isActive()));

        listAndCriteria.add(Criteria.where("mark").is(quizQuery.isMark()));

        if (Objects.nonNull(quizQuery.getDifficultlyLevel())) {
            listAndCriteria.add(Criteria.where("difficultlyLevel").is(quizQuery.getDifficultlyLevel()));
        }

        if (Objects.nonNull(quizQuery.getNumOfCorrectAnswer())) {
            listAndCriteria.add(Criteria.where("numOfCorrectAnswer").is(quizQuery.getNumOfCorrectAnswer()));
        }

        criteria = criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));

        return criteria;
    }

//    private List<Criteria> createListCriteria(QuizQuery quizQuery) {
//        List<Criteria> listAndCriteria = new ArrayList<>();
//
//        return listAndCriteria;
//    }
}
