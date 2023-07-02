package com.vuongle.imagine.models;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Document("quiz")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Quiz implements Serializable {

    @Id
    private ObjectId id;

    private List<ObjectId> listQuestionId;

    private List<Question> questions;

    @Size(min = 2, max = 100, message = "The title must be between 2 and 100 messages.")
    private String title;

    @Size(max = 500, message = "The description can't be longer than 500 characters.")
    private String description;

    private Integer numOfQuestion;

    private boolean isPublished;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

    private User createdBy;
}
