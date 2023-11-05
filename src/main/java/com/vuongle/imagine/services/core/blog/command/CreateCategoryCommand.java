package com.vuongle.imagine.services.core.blog.command;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryCommand implements Serializable {

    @NotNull(message = "Name must not be null")
    private String name;
}
