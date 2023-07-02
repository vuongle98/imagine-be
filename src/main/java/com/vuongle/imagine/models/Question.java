package com.vuongle.imagine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vuongle.imagine.constants.QuestionType;
import com.vuongle.imagine.dto.quiz.UserCheckQuiz;
import com.vuongle.imagine.models.embeded.Answer;
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

@Document("question")
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Question implements Serializable {
    @Id
    private ObjectId id;

    private QuestionType type = QuestionType.YES_NO;

    private String title;

    private boolean active;

    private List<Answer> answers;

    private boolean mark;

    private Integer difficultlyLevel = 1;

    private List<Answer> correctAnswer;

    private User createdBy;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

    public Integer checkAnswer(UserCheckQuiz checkQuiz) {
        int correct = 0;
        for (Answer answer: correctAnswer) {
            if (checkQuiz.getAnswerIds().stream().anyMatch(answerId -> answerId.equals(answer.getId()))) {
                correct += 1;
            }
        }

        return correct;
    }

}
