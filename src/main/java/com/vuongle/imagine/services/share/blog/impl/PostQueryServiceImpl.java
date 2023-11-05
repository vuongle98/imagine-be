package com.vuongle.imagine.services.share.blog.impl;

import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.Post;
import com.vuongle.imagine.services.share.blog.PostQueryService;
import com.vuongle.imagine.services.share.blog.query.PostQuery;
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
public class PostQueryServiceImpl implements PostQueryService {

    private final MongoTemplate mongoTemplate;

    public PostQueryServiceImpl(
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
    public <T> Page<T> findPage(PostQuery postQuery, Pageable pageable, Class<T> returnType) {

        Query query = createQuery(postQuery, pageable);
        List<T> data = mongoTemplate.find(query, returnType);

        return PageableExecutionUtils.getPage(
                data,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(PostQuery postQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(PostQuery postQuery, Class<T> returnType) {
        Query query = createQuery(postQuery);
        return mongoTemplate.find(query, returnType);
    }

    @Override
    public <T> List<T> findList(PostQuery postQuery, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public Query createQuery(PostQuery postQuery, Pageable pageable) {

        Query query = new Query();

        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }

        query.addCriteria(createCriteria(postQuery));

        return query;
    }

    @Override
    public Query createQuery(PostQuery postQuery) {
        return createQuery(postQuery, null);
    }

    @Override
    public long countByQuery(PostQuery postQuery) {
        return mongoTemplate.count(createQuery(postQuery), Post.class);
    }

    @Override
    public long countByQuery(PostQuery query, AggregationOperation... aggregationOperationInputs) {
        return 0;
    }

    @Override
    public Criteria createCriteria(PostQuery postQuery) {
        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(postQuery.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(postQuery.getId()));
        }

        if (Objects.nonNull(postQuery.getLikeTitle())) {
            listAndCriteria.add(Criteria.where("title").regex(postQuery.getLikeTitle(), "i"));
        }

        if (Objects.nonNull(postQuery.getInIds())) {
            listAndCriteria.add(Criteria.where("id").in(postQuery.getInIds()));
        }

        if (Objects.nonNull(postQuery.getLikeDescription())) {
            listAndCriteria.add(Criteria.where("description").regex(postQuery.getLikeDescription(), "i"));
        }

        if (Objects.nonNull(postQuery.getCategoryId())) {
            listAndCriteria.add(Criteria.where("category._id").is(postQuery.getCategoryId()));
        }

        if (Objects.nonNull(postQuery.getInCategoryIds())) {
            listAndCriteria.add(Criteria.where("category._id").in(postQuery.getInCategoryIds()));
        }

        // default is false
        if (Objects.nonNull(postQuery.getGetDeleted())) {
            if (Context.hasModifyPermission()) {
                listAndCriteria.add(Criteria.where("deletedAt").exists(postQuery.getGetDeleted()));
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
