package com.vuongle.imagine.dto.blog;

import com.vuongle.imagine.dto.storage.FileDto;
import com.vuongle.imagine.models.embeded.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("comment")
public class CommentDto implements Serializable {

    @Id
    private ObjectId id;

    private String content;

    private ObjectId postId;

    private Creator creator;

    private FileDto file;

    private ObjectId categoryId;

    private Instant deletedAt;

    private ObjectId parentId;
}
