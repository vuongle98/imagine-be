package com.vuongle.imagine.dto.quiz;

import com.vuongle.imagine.constants.QuizLevel;
import com.vuongle.imagine.models.File;
import com.vuongle.imagine.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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

    private File image;

    private Integer numOfQuestion;

    private boolean isPublished;

    private Instant createdDate;

    private Instant updatedDate;

    private String createdBy;

    private Integer countDown;

    private QuizLevel level;
}
