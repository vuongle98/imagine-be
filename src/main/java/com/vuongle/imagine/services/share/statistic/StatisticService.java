package com.vuongle.imagine.services.share.statistic;

import com.vuongle.imagine.dto.statistic.FullStatistic;

public interface StatisticService {

    FullStatistic statistic();

    void statisticUser();

    void statisticQuiz();

    void statisticQuestion();

    void statisticQuizHistory();
}
