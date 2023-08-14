package com.vuongle.imagine.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class QuizResult implements Serializable {

    private Integer totalAnswers = 0;
    private Integer numOfCorrectAnswers = 0;
    private List<UserCheckQuiz> answers;
}
