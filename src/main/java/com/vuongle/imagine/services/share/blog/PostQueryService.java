package com.vuongle.imagine.services.share.blog;

import com.vuongle.imagine.dto.blog.PostDto;
import com.vuongle.imagine.dto.statistic.PostStatistic;
import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.blog.query.PostQuery;
import org.bson.types.ObjectId;

public interface PostQueryService extends BaseService<PostQuery> {

    PostDto getById(ObjectId id);

    PostStatistic statistic();
}
