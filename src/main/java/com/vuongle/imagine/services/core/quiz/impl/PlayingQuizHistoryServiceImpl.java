package com.vuongle.imagine.services.core.quiz.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.PlayingQuizHistory;
import com.vuongle.imagine.repositories.PlayingQuizHistoryRepository;
import com.vuongle.imagine.services.core.quiz.PlayingQuizHistoryService;
import com.vuongle.imagine.services.core.quiz.command.CreatePlayingQuizHistoryCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdatePlayingQuizHistoryCommand;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class PlayingQuizHistoryServiceImpl implements PlayingQuizHistoryService {

    private final ObjectMapper objectMapper;

    private final PlayingQuizHistoryRepository playingQuizHistoryRepository;

    public PlayingQuizHistoryServiceImpl(
            PlayingQuizHistoryRepository playingQuizHistoryRepository,
            ObjectMapper objectMapper
    ) {
        this.playingQuizHistoryRepository = playingQuizHistoryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public PlayingQuizHistory createPlayingQuizHistory(CreatePlayingQuizHistoryCommand quizCommand) {
        PlayingQuizHistory playingQuizHistory = objectMapper.convertValue(quizCommand, PlayingQuizHistory.class);
        return playingQuizHistoryRepository.save(playingQuizHistory);
    }

    @Override
    public PlayingQuizHistory updatePlayingQuizHistory(UpdatePlayingQuizHistoryCommand quizCommand) {

        PlayingQuizHistory existingPlayingQuizHistory = playingQuizHistoryRepository.findById(quizCommand.getId()).orElse(null);
        if (Objects.isNull(existingPlayingQuizHistory)) {
            throw new DataNotFoundException("Playing quiz history not found");
        }

        if (Objects.nonNull(quizCommand.getQuizId())) {
            existingPlayingQuizHistory.setQuizId(quizCommand.getQuizId());
        }

        if (Objects.nonNull(quizCommand.getResult())) {
            existingPlayingQuizHistory.setResult(quizCommand.getResult());
        }

        if (Objects.nonNull(quizCommand.getUserId())) {
            existingPlayingQuizHistory.setUserId(quizCommand.getUserId());
        }

        return playingQuizHistoryRepository.save(existingPlayingQuizHistory);
    }

    @Override
    public void deletePlayingQuizHistory(ObjectId id) {
        playingQuizHistoryRepository.deleteById(id);
    }
}
