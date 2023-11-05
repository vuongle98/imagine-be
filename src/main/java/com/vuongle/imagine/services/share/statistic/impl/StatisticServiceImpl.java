package com.vuongle.imagine.services.share.statistic.impl;

import com.vuongle.imagine.dto.statistic.FullStatistic;
import com.vuongle.imagine.services.share.statistic.StatisticService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {


    private final MongoTemplate mongoTemplate;

    public StatisticServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public FullStatistic statistic() {
        return new FullStatistic();
    }

    @Override
    public void statisticUser() {


    }

    @Override
    public void statisticQuiz() {

    }

    @Override
    public void statisticQuestion() {

    }

    @Override
    public void statisticQuizHistory() {

    }
}
