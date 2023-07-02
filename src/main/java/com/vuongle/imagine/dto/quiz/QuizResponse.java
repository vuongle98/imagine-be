package com.vuongle.imagine.dto.quiz;

import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.models.User;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("quiz")
public class QuizResponse implements Serializable {

    @Id
    private ObjectId id;

    private List<QuestionResponse> questions;

    private String title;

    private String description;

    private Integer numOfQuestion;

    private boolean isPublished;

    private Instant createdDate;

    private Instant updatedDate;

    private User createdBy;
}