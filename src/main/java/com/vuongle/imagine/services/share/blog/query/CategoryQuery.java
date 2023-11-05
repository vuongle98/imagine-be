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
public class CategoryQuery implements Serializable {

    private ObjectId id;

    private List<ObjectId> inIds;

    private String likeName;

    private Boolean getDeleted = false;
}
