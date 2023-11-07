package com.vuongle.imagine.services.core.blog.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryCommand implements Serializable {

    @NotNull(message = "Name must not be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
}
