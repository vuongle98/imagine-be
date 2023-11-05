package com.vuongle.imagine.services.core.blog;

import com.vuongle.imagine.dto.blog.CommentDto;
import com.vuongle.imagine.services.core.BaseService;
import com.vuongle.imagine.services.core.blog.command.CreateCommentCommand;
import com.vuongle.imagine.services.core.blog.command.UpdateCommentCommand;
import org.bson.types.ObjectId;

public interface CommentService extends BaseService<CreateCommentCommand, UpdateCommentCommand, CommentDto> {


    void deleteByPostId(ObjectId postId, boolean isDelete);

    void deleteByCategoryId(ObjectId categoryId, boolean isDelete);

    void recoverByPostId(ObjectId postId);

    void recoverByCategoryId(ObjectId categoryId);

}
