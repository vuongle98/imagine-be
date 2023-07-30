package com.vuongle.imagine.services.share.todo.impl;

import com.vuongle.imagine.services.share.todo.TodoQueryService;
import com.vuongle.imagine.services.share.todo.query.TodoQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public class TodoQueryServiceImpl implements TodoQueryService {
    @Override
    public <T> Optional<T> findById(ObjectId id, Class<T> classType) {
        return Optional.empty();
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> classType) {
        return null;
    }

    @Override
    public <T> Page<T> findPage(TodoQuery query, Pageable pageable, Class<T> returnType) {
        return null;
    }

    @Override
    public <T> Page<T> findPage(TodoQuery query, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(TodoQuery query, Class<T> returnType) {
        return null;
    }

    @Override
    public Query createQuery(TodoQuery query, Pageable pageable) {
        return null;
    }

    @Override
    public Query createQuery(TodoQuery query) {
        return null;
    }

    @Override
    public long countByQuery(TodoQuery Query) {
        return 0;
    }

    @Override
    public Criteria createCriteria(TodoQuery query) {
        return null;
    }
}
