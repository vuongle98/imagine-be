package com.vuongle.imagine.services.core.blog.command;

import com.vuongle.imagine.dto.storage.FileDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostCommand {

    @Size(min = 10, max = 255, message = "Title must be between 10 and 255 characters")
    @NotNull
    private String title;

    @Size(min = 10, message = "Content must be at least 10 characters")
    @NotNull
    private String content;

    @Size(min = 10, message = "Description must be at least 10 characters")
    @NotNull
    private String description;

    private FileDto file;

    @NotNull(message = "Category must not be null")
    private ObjectId categoryId;
}
