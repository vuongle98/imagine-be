package com.vuongle.imagine.services.share.auth.impl;

import com.vuongle.imagine.services.share.auth.UserQueryService;
import com.vuongle.imagine.services.share.auth.query.UserQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserQueryServiceImpl implements UserQueryService {


    public UserQueryServiceImpl(

    ) {

    }

    @Override
    public <T> Optional<T> findById(ObjectId id, Class<T> classType) {
        return Optional.empty();
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> classType) {
        return null;
    }

    @Override
    public <T> Page<T> findPage(UserQuery query, Pageable pageable, Class<T> returnType) {
        return null;
    }

    @Override
    public <T> Page<T> findPage(UserQuery query, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(UserQuery query, Class<T> returnType) {
        return null;
    }

    @Override
    public Query createQuery(UserQuery query, Pageable pageable) {
        return null;
    }

    @Override
    public Query createQuery(UserQuery query) {
        return null;
    }

    @Override
    public long countByQuery(UserQuery Query) {
        return 0;
    }

    @Override
    public long countByQuery(UserQuery query, AggregationOperation... aggregationOperationInputs) {
        return 0;
    }

    @Override
    public Criteria createCriteria(UserQuery query) {
        return null;
    }
}
