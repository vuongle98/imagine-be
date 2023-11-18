package com.vuongle.imagine.services.share.statistic.impl;

import com.vuongle.imagine.dto.statistic.FullStatistic;
import com.vuongle.imagine.dto.statistic.PostStatistic;
import com.vuongle.imagine.services.share.auth.UserQueryService;
import com.vuongle.imagine.services.share.blog.CategoryQueryService;
import com.vuongle.imagine.services.share.blog.PostQueryService;
import com.vuongle.imagine.services.share.quiz.QuestionQueryService;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.statistic.StatisticService;
import com.vuongle.imagine.services.share.storage.FileQueryService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {


    private final MongoTemplate mongoTemplate;

    private final PostQueryService postQueryService;

    private final FileQueryService fileQueryService;

    private final QuizQueryService quizQueryService;

    private final QuestionQueryService questionQueryService;

    private final UserQueryService userQueryService;

    private final CategoryQueryService categoryQueryService;

    public StatisticServiceImpl(
            MongoTemplate mongoTemplate,
            PostQueryService postQueryService,
            FileQueryService fileQueryService,
            QuizQueryService quizQueryService,
            QuestionQueryService questionQueryService,
            UserQueryService userQueryService,
            CategoryQueryService categoryQueryService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.postQueryService = postQueryService;
        this.fileQueryService = fileQueryService;
        this.quizQueryService = quizQueryService;
        this.questionQueryService = questionQueryService;
        this.userQueryService = userQueryService;
        this.categoryQueryService = categoryQueryService;
    }

    @Override
    public FullStatistic statistic() {

        PostStatistic postStatistic = postQueryService.statistic();

        FullStatistic fullStatistic = new FullStatistic();

        fullStatistic.setPostStatistic(postStatistic);

        return fullStatistic;
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
