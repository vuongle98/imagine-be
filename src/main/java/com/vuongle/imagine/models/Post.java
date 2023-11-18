package com.vuongle.imagine.models;

import com.vuongle.imagine.dto.blog.CategoryDto;
import com.vuongle.imagine.dto.storage.FileDto;
import com.vuongle.imagine.models.embeded.Creator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("post")
public class Post implements Serializable {

    @Id
    @NotNull
    private ObjectId id;

    @NotNull
    @Indexed(unique = true, sparse = true, name = "unique_title", background = true)
    private String title;

    private String slug;

    @NotNull
    private String description;

    @NotNull
    private String content;

    private FileDto file;

    private Instant publishedAt;

    private int numLikes;

    private int numComment;

    private boolean featured;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Creator creator;

    private Instant deletedAt;

    @CreatedDate
    private Instant createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;
}
