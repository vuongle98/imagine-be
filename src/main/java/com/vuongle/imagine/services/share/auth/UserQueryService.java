package com.vuongle.imagine.services.share.auth;

import com.vuongle.imagine.dto.auth.UserProfile;
import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.auth.query.UserQuery;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.List;

public interface UserQueryService extends BaseService<UserQuery> {

    List<UserProfile> findList(UserQuery userQuery);

    <T> T findByCriteria(UserQuery userQuery, Class<T> classType, AggregationOperation ...aggregationOperationInputs);

    UserProfile findById(ObjectId id);

    UserProfile findByUsername(String username);
}
