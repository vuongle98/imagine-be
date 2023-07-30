package com.vuongle.imagine.dto.quiz;

import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.constants.QuestionType;
import com.vuongle.imagine.models.embeded.BaseAnswer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("question")
public class QuestionResponse {

    @Id
    private ObjectId id;

    private QuestionType type = QuestionType.YES_NO;

    private String title;

    private boolean active;

    private List<BaseAnswer> answers;

    private boolean mark;

    private QuizCategory category;

    private String createdBy;

    private Instant createdDate;

    private Instant updatedDate;
}
