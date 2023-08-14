package com.vuongle.imagine.services.share.quiz;

import com.vuongle.imagine.models.PlayingQuizHistory;
import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.quiz.query.PlayingQuizHistoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayingQuizHistoryQueryService extends BaseService<PlayingQuizHistoryQuery> {

    Page<PlayingQuizHistory> findByCurrentUser(PlayingQuizHistoryQuery quizHistoryQuery, Pageable pageable);

}
