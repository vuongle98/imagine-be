package com.vuongle.imagine.services.core.crawl;

import com.vuongle.imagine.dto.crawl.NeedCrawlData;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.models.Quiz;

import java.io.IOException;
import java.util.List;

public interface CrawlQuizQTMService {
    List<NeedCrawlData> getListNeedCrawlData(String url) throws IOException;

    List<Question> crawlQuestion(NeedCrawlData needCrawlData) throws IOException;

    List<Quiz> saveQuizs(List<NeedCrawlData> needCrawlDataList) throws IOException;

    void crawlAndSaveQuiz(String url, Integer numOfPage) throws IOException;
}
