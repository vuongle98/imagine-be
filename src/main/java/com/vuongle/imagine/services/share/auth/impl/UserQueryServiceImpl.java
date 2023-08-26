package com.vuongle.imagine.services.share.auth.impl;

import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.repositories.UserRepository;
import com.vuongle.imagine.services.share.auth.UserQueryService;
import com.vuongle.imagine.services.share.auth.query.UserQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class UserQueryServiceImpl implements UserQueryService {

    private final MongoTemplate mongoTemplate;

    private final UserRepository userRepository;

    public UserQueryServiceImpl(
            MongoTemplate mongoTemplate,
            UserRepository userRepository
    ) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
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
    public <T> Page<T> findPage(UserQuery query, Pageable pageable, Class<T> returnType) {
        return null;
    }

    @Override
    public <T> Page<T> findPage(UserQuery query, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(UserQuery userQuery, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {

        Criteria criteria = createCriteria(userQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.addAll(Stream.of(aggregationOperationInputs).toList());
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        return mongoTemplate.aggregate(aggregation, User.class, returnType).getMappedResults();
    }

    @Override
    public <T> List<T> findList(UserQuery userQuery, Class<T> returnType) {
        Query query = createQuery(userQuery);
        return mongoTemplate.find(query, returnType);
    }

    @Override
    public Query createQuery(UserQuery userQuery, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(createCriteria(userQuery));
        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }
        return query;
    }

    @Override
    public Query createQuery(UserQuery userQuery) {
        return createQuery(userQuery, null);
    }

    @Override
    public long countByQuery(UserQuery query) {
        Query mongoQuery = createQuery(query);
        return mongoTemplate.count(mongoQuery, User.class);
    }

    @Override
    public long countByQuery(UserQuery query, AggregationOperation... aggregationOperationInputs) {
        Query mongoQuery = createQuery(query);
        return mongoTemplate.count(mongoQuery, User.class);
    }

    @Override
    public Criteria createCriteria(UserQuery query) {
        Criteria criteria = new Criteria();
        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(query.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(query.getId()));
        }

        if (Objects.nonNull(query.getUsername())) {
            listAndCriteria.add(Criteria.where("username").is(query.getUsername()));
        }

        if (Objects.nonNull(query.getEmail())) {
            listAndCriteria.add(Criteria.where("email").is(query.getEmail()));
        }

        if (Objects.nonNull(query.getFullName())) {
            listAndCriteria.add(Criteria.where("fullName").regex(query.getFullName(), "i"));
        }

        if (Objects.nonNull(query.getFriendIds())) {
            listAndCriteria.add(Criteria.where("friendIds").in(query.getFriendIds()));
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }

    @Override
    public List<UserProfile> findList(UserQuery userQuery) {

        AggregationOperation lookup = Aggregation.lookup(
                "user", "friendIds", "_id", "friends"
        );

        return findList(userQuery, UserProfile.class, lookup);
    }

    @Override
    public <T> T findByCriteria(UserQuery userQuery, Class<T> classType, AggregationOperation... aggregationOperationInputs) {

        Criteria criteria = createCriteria(userQuery);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.match(criteria));
        aggregationOperations.addAll(Stream.of(aggregationOperationInputs).toList());
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        return mongoTemplate.aggregate(aggregation, UserProfile.class, classType).getUniqueMappedResult();
    }

    @Override
    public UserProfile findById(ObjectId id) {

        AggregationOperation lookup = Aggregation.lookup(
                "user", "friendIds", "_id", "friends"
        );

        UserQuery query = new UserQuery();
        query.setId(id);

        return findByCriteria(query, UserProfile.class, lookup);
    }

    @Override
    public UserProfile findByUsername(String username) {
        AggregationOperation lookup = Aggregation.lookup(
                "user", "friendIds", "_id", "friends"
        );

        UserQuery query = new UserQuery();
        query.setUsername(username);

        return findByCriteria(query, UserProfile.class, lookup);
    }
}
