package com.vuongle.imagine.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullStatistic implements Serializable {

    private UserStatistic userStatistic;
    private QuizStatistic quizStatistic;
    private FileStatistic fileStatistic;
    private QuizPlayingStatistic quizPlayingStatistic;
    private ConversationStatistic conversationStatistic;
    private QuestionStatistic questionStatistic;

}
