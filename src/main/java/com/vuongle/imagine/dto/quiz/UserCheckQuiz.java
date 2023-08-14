package com.vuongle.imagine.dto.quiz;

import com.vuongle.imagine.models.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCheckQuiz {

    private ObjectId questionId;

    private List<ObjectId> answerIds;

    private List<ObjectId> correctAnswerIds;

    private QuestionResponse question;
}
