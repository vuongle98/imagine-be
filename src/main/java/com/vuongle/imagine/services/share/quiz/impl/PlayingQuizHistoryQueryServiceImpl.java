package com.vuongle.imagine.services.share.quiz.impl;

import com.vuongle.imagine.dto.common.CountResult;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.exceptions.UserNotFoundException;
import com.vuongle.imagine.models.PlayingQuizHistory;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.services.share.quiz.PlayingQuizHistoryQueryService;
import com.vuongle.imagine.services.share.quiz.query.PlayingQuizHistoryQuery;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PlayingQuizHistoryQueryServiceImpl implements PlayingQuizHistoryQueryService {

    private final MongoTemplate mongoTemplate;

    public PlayingQuizHistoryQueryServiceImpl(
            MongoTemplate mongoTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public <T> Optional<T> findById(ObjectId id, Class<T> classType) {
        return Optional.ofNullable(mongoTemplate.findById(id, classType));
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> classType) {
        return findById(id, classType).orElseThrow(() -> new DataNotFoundException("Not found data has id " + id));
    }

    @Override
    public <T> Page<T> findPage(PlayingQuizHistoryQuery quizHistoryQuery, Pageable pageable, Class<T> returnType) {
        Query query = createQuery(quizHistoryQuery, pageable);
        List<T> data = mongoTemplate.find(query, returnType);

        return PageableExecutionUtils.getPage(
                data,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(PlayingQuizHistoryQuery quizHistoryQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        Criteria criteria = createCriteria(quizHistoryQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            aggregationOperations.add(Aggregation.sort(pageable.getSort()));
        }

        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.addAll(Arrays.stream(aggregationOperationInputs).filter(Objects::nonNull).toList());
        aggregationOperations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        List<Object> results = mongoTemplate.aggregate(aggregation, returnType, Object.class).getMappedResults();
        System.out.println(results);
        return null; // new PageImpl<>(results, pageable, countByQuery(quizHistoryQuery, aggregationOperationInputs));
    }

    @Override
    public <T> List<T> findList(PlayingQuizHistoryQuery quizHistoryQuery, Class<T> returnType) {
        Query query = createQuery(quizHistoryQuery);
        return mongoTemplate.find(query, returnType);
    }

    @Override
    public <T> List<T> findList(PlayingQuizHistoryQuery query, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public Query createQuery(PlayingQuizHistoryQuery quizHistoryQuery, Pageable pageable) {
        Query query = new Query();
        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }

        query.addCriteria(createCriteria(quizHistoryQuery));

        return query;
    }

    @Override
    public Query createQuery(PlayingQuizHistoryQuery query) {
        return createQuery(query, null);
    }

    @Override
    public long countByQuery(PlayingQuizHistoryQuery quizHistoryQuery) {
        Query query = createQuery(quizHistoryQuery);
        return mongoTemplate.count(query, PlayingQuizHistory.class);
    }

    @Override
    public long countByQuery(PlayingQuizHistoryQuery quizHistoryQuery, AggregationOperation... aggregationOperationInputs) {
        Criteria criteria = createCriteria(quizHistoryQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();

        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.addAll(Arrays.stream(aggregationOperationInputs).filter(Objects::nonNull).toList());
        aggregationOperations.add(Aggregation.count().as("count"));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        AggregationResults<CountResult> countResults = mongoTemplate.aggregate(aggregation, PlayingQuizHistory.class, CountResult.class);

        if (Objects.nonNull(countResults.getUniqueMappedResult())) {
            return countResults.getUniqueMappedResult().getCount();
        }

         return 0;
    }


    @Override
    public Criteria createCriteria(PlayingQuizHistoryQuery query) {
        List<Criteria> listAndCriteria = new ArrayList<>();
        Criteria criteria = new Criteria();

        if (Objects.nonNull(query.getQuizId())) {
            listAndCriteria.add(Criteria.where("quizId").is(query.getQuizId()));
        }

        if (Objects.nonNull(query.getUserId())) {
            listAndCriteria.add(Criteria.where("userId").is(query.getUserId()));
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }

    @Override
    public Page<PlayingQuizHistory> findByCurrentUser(PlayingQuizHistoryQuery quizHistoryQuery, Pageable pageable) {
        User user = Context.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found");
        }

        quizHistoryQuery.setUserId(user.getId());

        AggregationOperation unwindAnswers = Aggregation.unwind("$result.answers");
        AggregationOperation lookup = Aggregation.lookup("question", "result.answers.questionId", "_id", "result.answers.question");
        AggregationOperation unwindQuestion = Aggregation.unwind("$result.answers.question");
        AggregationOperation group = Aggregation
                .group("_id")
                .first("userId").as("userId")
                .first("quizId").as("quizId")
                .push("result").as("result");

        return findPage(quizHistoryQuery, pageable, PlayingQuizHistory.class, unwindAnswers, lookup, unwindQuestion, group);

    }
}
