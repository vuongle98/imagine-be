package com.vuongle.imagine.services.core.quiz;

import com.vuongle.imagine.models.PlayingQuizHistory;
import com.vuongle.imagine.services.core.quiz.command.CreatePlayingQuizHistoryCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdatePlayingQuizHistoryCommand;
import org.bson.types.ObjectId;

public interface PlayingQuizHistoryService {

    PlayingQuizHistory createPlayingQuizHistory(CreatePlayingQuizHistoryCommand quizCommand);

    PlayingQuizHistory updatePlayingQuizHistory(UpdatePlayingQuizHistoryCommand quizCommand);

    void deletePlayingQuizHistory(ObjectId id);
}
