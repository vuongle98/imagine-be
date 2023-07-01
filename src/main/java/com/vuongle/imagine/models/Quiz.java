package com.vuongle.imagine.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.vuongle.imagine.constants.QuizType;
import com.vuongle.imagine.models.embeded.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("quiz")
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Quiz implements Serializable {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private QuizType type = QuizType.YES_NO;

    private String question;

    private boolean active;

    private List<Answer> answers;

    private boolean mark;

    private Integer difficultlyLevel = 1;

    private Integer numOfCorrectAnswer = 1;
}
