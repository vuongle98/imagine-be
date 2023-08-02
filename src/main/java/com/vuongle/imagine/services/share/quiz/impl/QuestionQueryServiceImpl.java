package com.vuongle.imagine.services.share.quiz.impl;

import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.repositories.QuestionRepository;
import com.vuongle.imagine.services.share.quiz.QuestionQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuestionQuery;
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
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final QuestionRepository questionRepository;

    private final MongoTemplate mongoTemplate;

    public QuestionQueryServiceImpl(
            QuestionRepository questionRepository,
            MongoTemplate mongoTemplate
    ) {
        this.questionRepository = questionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Question> findQuestionById(ObjectId id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question getQuestionById(ObjectId id) {
        return findQuestionById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy quiz " + id));
    }

    @Override
    public Page<Question> findPageQuestion(QuestionQuery questionQuery, Pageable pageable) {
        Query query = createQuery(pageable, questionQuery);

        List<Question> listQuestion = mongoTemplate.find(query, Question.class);

        return PageableExecutionUtils.getPage(
                listQuestion,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Question.class)
        );
    }

    @Override
    public Page<Question> findPageQuestion(QuestionQuery questionQuery, Pageable pageable, AggregationOperation... aggregationOperationInputs) {
        Criteria criteria = createCriteria(questionQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            aggregationOperations.add(Aggregation.sort(pageable.getSort()));
        }

        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.addAll(Arrays.stream(aggregationOperationInputs).toList());
        aggregationOperations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        List<Question> results = mongoTemplate.aggregate(aggregation, Question.class, Question.class).getMappedResults();
        return new PageImpl<>(results, pageable, countByCriteria(questionQuery));

    }

    @Override
    public List<Question> findListQuestion(QuestionQuery questionQuery) {
        Query query = createQuery(questionQuery);
        return mongoTemplate.find(query, Question.class);
    }

    @Override
    public long countByCriteria(QuestionQuery questionQuery) {
        Query query = createQuery(questionQuery);
        return mongoTemplate.count(Query.of(query), Question.class);
    }


    private Query createQuery(Pageable pageable, QuestionQuery questionQuery) {
        Criteria criteria = createCriteria(questionQuery);

        Query query = new Query();
        query.addCriteria(criteria);
        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }
        return query;
    }

    @Override
    public <T> Optional<T> findById(ObjectId id, Class<T> classType) {
        return Optional.ofNullable(mongoTemplate.findById(id, classType));
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> classType) {
        return findById(id, classType).orElseThrow(() -> new DataNotFoundException("Not found question " + id));
    }

    @Override
    public <T> Page<T> findPage(QuestionQuery questionQuery, Pageable pageable, Class<T> returnType) {
        Query query = createQuery(questionQuery, pageable);
        List<T> data = mongoTemplate.find(query, returnType);

        return PageableExecutionUtils.getPage(
                data,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(QuestionQuery questionQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(QuestionQuery questionQuery, Class<T> returnType) {
        Query query = createQuery(questionQuery);
        return mongoTemplate.find(query, returnType);
    }

    @Override
    public Query createQuery(QuestionQuery questionQuery, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(createCriteria(questionQuery));
        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }
        return query;
    }

    @Override
    public Query createQuery(QuestionQuery questionQuery) {
        return createQuery(null, questionQuery);
    }

    @Override
    public long countByQuery(QuestionQuery questionQuery) {
        Query query = createQuery(questionQuery);
        return mongoTemplate.count(query, Question.class);
    }

    @Override
    public Criteria createCriteria(QuestionQuery questionQuery) {
        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(questionQuery.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(questionQuery.getId()));
        }

        if (Objects.nonNull(questionQuery.getLikeTitle())) {
            listAndCriteria.add(Criteria.where("title").regex(questionQuery.getLikeTitle(), "i"));
        }

        if (Objects.nonNull(questionQuery.getMark())) {
            listAndCriteria.add(Criteria.where("mark").is(questionQuery.getMark()));
        }

        if (Objects.nonNull(questionQuery.getActive())) {
            listAndCriteria.add(Criteria.where("active").is(questionQuery.getActive()));
        }

        if (Objects.nonNull(questionQuery.getDifficultlyLevel())) {
            listAndCriteria.add(Criteria.where("difficultlyLevel").is(questionQuery.getDifficultlyLevel()));
        }

        if (Objects.nonNull(questionQuery.getCategory())) {
            listAndCriteria.add(Criteria.where("category").is(questionQuery.getCategory()));
        }

        if (Objects.nonNull(questionQuery.getType())) {
            listAndCriteria.add(Criteria.where("type").is(questionQuery.getType()));
        }

        if (Objects.nonNull(questionQuery.getCreatedBy())) {
            listAndCriteria.add(Criteria.where("createdBy").is(questionQuery.getCreatedBy()));
        }

        if (Objects.nonNull(questionQuery.getListId())) {
            listAndCriteria.add(Criteria.where("_id").in(questionQuery.getListId()));
        }

//        if (Objects.nonNull(questionQuery.getNumOfCorrectAnswer())) {
//            listAndCriteria.add(Criteria.where("numOfCorrectAnswer").is(questionQuery.getNumOfCorrectAnswer()));
//        }

        criteria = criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));

        return criteria;
    }
}
