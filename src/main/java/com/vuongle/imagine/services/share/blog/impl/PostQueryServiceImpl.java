package com.vuongle.imagine.services.share.blog.impl;

import com.vuongle.imagine.dto.blog.PostDto;
import com.vuongle.imagine.dto.common.CountResult;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.Post;
import com.vuongle.imagine.services.share.blog.CommentQueryService;
import com.vuongle.imagine.services.share.blog.PostQueryService;
import com.vuongle.imagine.services.share.blog.query.PostQuery;
import com.vuongle.imagine.utils.Context;
import com.vuongle.imagine.utils.ValidateDataSource;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostQueryServiceImpl implements PostQueryService {

    private final MongoTemplate mongoTemplate;

    private final CommentQueryService commentQueryService;

    public PostQueryServiceImpl(
            MongoTemplate mongoTemplate,
            CommentQueryService commentQueryService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.commentQueryService = commentQueryService;
    }

    @Override
    public PostDto getById(ObjectId id) {
        PostDto post = getById(id, PostDto.class);

        if (!ValidateDataSource.isOwnData(post, PostDto.class)) {
            throw new DataNotFoundException("Not found data has id " + id);
        }

        post.setComments(commentQueryService.getCommentsByPostId(id));

        return post;
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
        Criteria criteria = createCriteria(postQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            aggregationOperations.add(Aggregation.sort(pageable.getSort()));
        }

        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.addAll(Arrays.stream(aggregationOperationInputs).toList());
        aggregationOperations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        List<T> results = mongoTemplate.aggregate(aggregation, Post.class, returnType).getMappedResults();
        return new PageImpl<>(results, pageable, countByQuery(postQuery, aggregationOperationInputs));
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
    public long countByQuery(PostQuery postQuery, AggregationOperation... aggregationOperationInputs) {
        Criteria criteria = createCriteria(postQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();

        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.addAll(Arrays.stream(aggregationOperationInputs).toList());
        aggregationOperations.add(Aggregation.count().as("count"));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        AggregationResults<CountResult> countResult = mongoTemplate.aggregate(aggregation, Post.class, CountResult.class);

        if (Objects.nonNull(countResult.getUniqueMappedResult())) {
            return countResult.getUniqueMappedResult().getCount();
        }

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
