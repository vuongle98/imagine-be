package com.vuongle.imagine.services.share.blog.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentQuery implements Serializable {

    private ObjectId creatorId;

    private ObjectId postId;

    private ObjectId categoryId;

    private Boolean getDeleted = false;
}
