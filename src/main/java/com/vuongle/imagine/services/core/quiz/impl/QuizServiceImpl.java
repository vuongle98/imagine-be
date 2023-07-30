package com.vuongle.imagine.services.core.quiz.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.exceptions.DataFormatException;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.repositories.QuizRepository;
import com.vuongle.imagine.services.core.quiz.QuizService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuizCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuizCommand;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final ObjectMapper objectMapper;

    private final QuizRepository quizRepository;

    private final QuizQueryService quizQueryService;

    public QuizServiceImpl(
            ObjectMapper objectMapper,
            QuizRepository quizRepository,
            QuizQueryService quizQueryService
    ) {
        this.objectMapper = objectMapper;
        this.quizRepository = quizRepository;
        this.quizQueryService = quizQueryService;
    }

    @Override
    public Quiz createQuiz(CreateQuizCommand command) {
        if (command.isValidateData()) {
            throw new DataFormatException("Data not valid");
        }

        Quiz quiz = objectMapper.convertValue(command, Quiz.class);

        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(UpdateQuizCommand command) {

        if (!command.isValidateData()) {
            throw new DataFormatException("Data not valid");
        }

        Quiz existedQuiz = quizQueryService.getById(command.getId());

        if (Objects.nonNull(command.getTitle())) {
            existedQuiz.setTitle(command.getTitle());
        }

        if (Objects.nonNull(command.getDescription())) {
            existedQuiz.setDescription(command.getDescription());
        }

        if (Objects.nonNull(command.getCountDown())) {
            existedQuiz.setCountDown(command.getCountDown());
        }

        if (Objects.nonNull(command.getCategory())) {
            existedQuiz.setCategory(command.getCategory());
        }

        if (Objects.nonNull(command.getLevel())) {
            existedQuiz.setLevel(command.getLevel());
        }

        existedQuiz.setListQuestionId(command.getListQuestionId());

        existedQuiz.setPublished(command.isPublished());
        existedQuiz.setMark(command.isMark());

        return quizRepository.save(existedQuiz);
    }

    @Override
    public void delete(ObjectId id) {
        quizRepository.deleteById(id);
    }
}
