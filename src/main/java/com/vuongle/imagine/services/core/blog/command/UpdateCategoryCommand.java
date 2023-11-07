package com.vuongle.imagine.services.core.blog.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryCommand implements Serializable {

    private ObjectId id;

    private String name;

    private boolean recover;
}
