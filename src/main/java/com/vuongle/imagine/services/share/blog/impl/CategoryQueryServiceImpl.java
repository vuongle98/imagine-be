package com.vuongle.imagine.services.share.blog.impl;

import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.Category;
import com.vuongle.imagine.services.share.blog.CategoryQueryService;
import com.vuongle.imagine.services.share.blog.query.CategoryQuery;
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
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final MongoTemplate mongoTemplate;

    public CategoryQueryServiceImpl(
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
    public <T> Page<T> findPage(CategoryQuery categoryQuery, Pageable pageable, Class<T> returnType) {
        Query query = createQuery(categoryQuery, pageable);
        List<T> data = mongoTemplate.find(query, returnType);

        return PageableExecutionUtils.getPage(
                data,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(CategoryQuery categoryQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(CategoryQuery categoryQuery, Class<T> returnType) {
        Query query = createQuery(categoryQuery);
        return mongoTemplate.find(query, returnType);
    }

    @Override
    public <T> List<T> findList(CategoryQuery categoryQuery, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public Query createQuery(CategoryQuery categoryQuery, Pageable pageable) {

        Query query = new Query();

        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }

        query.addCriteria(createCriteria(categoryQuery));

        return query;
    }

    @Override
    public Query createQuery(CategoryQuery categoryQuery) {
        return createQuery(categoryQuery, null);
    }

    @Override
    public long countByQuery(CategoryQuery categoryQuery) {
        Query query = createQuery(categoryQuery);
        return mongoTemplate.count(query, Category.class);
    }

    @Override
    public long countByQuery(CategoryQuery categoryQuery, AggregationOperation... aggregationOperationInputs) {
        return 0;
    }

    @Override
    public Criteria createCriteria(CategoryQuery categoryQuery) {
        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(categoryQuery.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(categoryQuery.getId()));
        }

        if (Objects.nonNull(categoryQuery.getLikeName())) {
            listAndCriteria.add(Criteria.where("name").regex(categoryQuery.getLikeName(), "i"));
        }

        if (Objects.nonNull(categoryQuery.getInIds())) {
            listAndCriteria.add(Criteria.where("id").in(categoryQuery.getInIds()));
        }

        if (Objects.nonNull(categoryQuery.getGetDeleted())) {
            if (!Context.hasModifyPermission()) {
                listAndCriteria.add(Criteria.where("deletedAt").exists(false));
            }
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }
}
