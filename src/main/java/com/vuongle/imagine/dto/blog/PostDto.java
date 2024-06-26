package com.vuongle.imagine.dto.blog;

import com.vuongle.imagine.dto.storage.FileDto;
import com.vuongle.imagine.models.embeded.Creator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("post")
public class PostDto implements Serializable {

    @Id
    @NotNull
    private ObjectId id;

    @NotNull
    private String title;

    private String slug;

    @NotNull
    private String description;

    @NotNull
    private String content;

    private FileDto file;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Creator creator;

    private Instant deletedAt;

    private Instant createdDate;

    private List<CommentDto> comments;

    private int numLikes;

    private int numComment;

    private Boolean featured;
}
