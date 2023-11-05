package com.vuongle.imagine.models;

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
@Document("file")
public class File implements Serializable {
    @Id
    @NotNull
    private ObjectId id;

    @NotNull
    private String fileName;

    @NotNull
    private String extension;

    @NotNull
    private String contentType;

    private long size;

    @NotNull
    private String path;

    @NotNull
    private String checksum;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @LastModifiedBy
    private String lastModifiedBy;
}
