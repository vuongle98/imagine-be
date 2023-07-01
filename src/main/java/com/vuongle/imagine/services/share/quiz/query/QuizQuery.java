package com.vuongle.imagine.services.share.quiz.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuery {
    private String likeQuestion;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private boolean active = true;

    private boolean mark = false;

    private String likeAnswer;

    private Integer difficultlyLevel;

    private Integer numOfCorrectAnswer;

}
