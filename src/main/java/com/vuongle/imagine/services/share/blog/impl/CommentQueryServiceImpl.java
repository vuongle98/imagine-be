package com.vuongle.imagine.services.share.blog.impl;

import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.Comment;
import com.vuongle.imagine.services.share.blog.CommentQueryService;
import com.vuongle.imagine.services.share.blog.query.CommentQuery;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentQueryServiceImpl implements CommentQueryService {

    private final MongoTemplate mongoTemplate;

    public CommentQueryServiceImpl(
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
    public <T> Page<T> findPage(CommentQuery commentQuery, Pageable pageable, Class<T> returnType) {

        Query query = createQuery(commentQuery, pageable);

        List<T> data = mongoTemplate.find(query, returnType);

        return PageableExecutionUtils.getPage(
                data,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(CommentQuery commentQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(CommentQuery commentQuery, Class<T> returnType) {

        Query query = createQuery(commentQuery);

        return mongoTemplate.find(query, returnType);
    }

    @Override
    public <T> List<T> findList(CommentQuery commentQuery, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public Query createQuery(CommentQuery commentQuery, Pageable pageable) {
        Query query = new Query();

        if (Objects.nonNull(pageable)) query.with(pageable);

        query.addCriteria(createCriteria(commentQuery));

        return query;
    }

    @Override
    public Query createQuery(CommentQuery commentQuery) {
        return createQuery(commentQuery, null);
    }

    @Override
    public long countByQuery(CommentQuery commentQuery) {
        Query query = createQuery(commentQuery);

        return mongoTemplate.count(query, Comment.class);
    }

    @Override
    public long countByQuery(CommentQuery commentQuery, AggregationOperation... aggregationOperationInputs) {
        return 0;
    }

    @Override
    public Criteria createCriteria(CommentQuery commentQuery) {
        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(commentQuery.getCreatorId())) {
            listAndCriteria.add(Criteria.where("creator.id").is(commentQuery.getCreatorId()));
        }

        if (Objects.nonNull(commentQuery.getGetDeleted())) {
            if (Context.hasModifyPermission()) {
                listAndCriteria.add(Criteria.where("deletedAt").exists(commentQuery.getGetDeleted()));
            } else {
                listAndCriteria.add(Criteria.where("deletedAt").exists(false));
            }
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }
}
