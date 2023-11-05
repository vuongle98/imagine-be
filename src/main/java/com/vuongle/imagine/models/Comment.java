package com.vuongle.imagine.models;

import com.vuongle.imagine.dto.storage.FileDto;
import com.vuongle.imagine.models.embeded.Creator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("comment")
public class Comment implements Serializable {

    @Id
    @NotNull
    private ObjectId id;

    @NotNull
    private String content;

    private FileDto file;

    private Instant deletedAt;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @NotNull
    private ObjectId postId;

    @NotNull
    private ObjectId categoryId;

    @NotNull
    private Creator creator;
}
