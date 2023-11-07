package com.vuongle.imagine.services.core.blog.command;

import com.vuongle.imagine.dto.storage.FileDto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostCommand implements Serializable {

    private ObjectId id;

    @Size(min = 10, max = 255, message = "Title must be between 10 and 255 characters")
    private String title;

    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;

    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    private FileDto file;

    private ObjectId categoryId;

    private boolean recover;
}
