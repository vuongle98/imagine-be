package com.vuongle.imagine.dto.blog;

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
@Document("category")
public class CategoryDto implements Serializable {

    @Id
    private ObjectId id;

    private String name;

    private String slug;

    private Instant deletedAt;
}
