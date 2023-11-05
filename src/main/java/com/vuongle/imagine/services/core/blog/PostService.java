package com.vuongle.imagine.services.core.blog;

import com.vuongle.imagine.dto.blog.PostDto;
import com.vuongle.imagine.dto.common.CommonResult;
import com.vuongle.imagine.models.Post;
import com.vuongle.imagine.services.core.BaseService;
import com.vuongle.imagine.services.core.blog.command.CreatePostCommand;
import com.vuongle.imagine.services.core.blog.command.UpdatePostCommand;
import org.bson.types.ObjectId;

public interface PostService extends BaseService<CreatePostCommand, UpdatePostCommand, PostDto> {

    void deleteByCategoryId(ObjectId categoryId, boolean isDelete);

    void recoverByCategoryId(ObjectId categoryId);

    void recover(ObjectId id);

    CommonResult<Post> updateCategory(ObjectId categoryId);
}
