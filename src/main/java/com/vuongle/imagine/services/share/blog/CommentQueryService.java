package com.vuongle.imagine.services.share.blog;

import com.vuongle.imagine.dto.blog.CommentDto;
import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.blog.query.CommentQuery;
import org.bson.types.ObjectId;

import java.util.List;

public interface CommentQueryService extends BaseService<CommentQuery> {

    List<CommentDto> getCommentsByPostId(ObjectId postId);
}
