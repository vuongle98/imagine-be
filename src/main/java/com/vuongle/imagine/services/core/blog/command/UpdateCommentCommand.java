package com.vuongle.imagine.services.core.blog.command;

import com.vuongle.imagine.dto.storage.FileDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentCommand implements Serializable {

    private ObjectId id;

    @Size(min = 10, max = 255, message = "Content must be between 10 and 255 characters")
    private String content;

    private FileDto file;

    private ObjectId postId;

    private ObjectId categoryId;
}
