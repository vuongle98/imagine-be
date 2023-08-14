package com.vuongle.imagine.services.share;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public interface BaseService<Q> {

    <T> Optional<T> findById(ObjectId id, Class<T> classType);

    <T> T getById(ObjectId id, Class<T> classType);

    <T> Page<T> findPage(Q query, Pageable pageable, Class<T> returnType);

    <T> Page<T> findPage(Q query, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs);

    <T> List<T> findList(Q query, Class<T> returnType);

    Query createQuery(Q query, Pageable pageable);

    Query createQuery(Q query);

    long countByQuery(Q Query);

    long countByQuery(Q query, AggregationOperation ... aggregationOperationInputs);

    Criteria createCriteria(Q query);
}
