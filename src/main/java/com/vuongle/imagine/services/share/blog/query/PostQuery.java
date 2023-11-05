package com.vuongle.imagine.services.share.blog.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostQuery implements Serializable {

    private ObjectId id;

    private List<ObjectId> inIds;

    private String likeTitle;

    private String likeDescription;

    private ObjectId categoryId;

    private List<ObjectId> inCategoryIds;

    private Boolean getDeleted = false;
}
