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

    private final QuizRepository quizRepository;

    private final QuizQueryService quizQueryService;

    private final ObjectMapper objectMapper;

    public QuizServiceImpl(
            QuizRepository quizRepository,
            ObjectMapper objectMapper,
            QuizQueryService quizQueryService
    ) {
        this.quizRepository = quizRepository;
        this.objectMapper = objectMapper;
        this.quizQueryService = quizQueryService;
    }

    @Override
    public Quiz createQuiz(CreateQuizCommand command) {
        if (!command.validateCreateData()) {
            throw new DataFormatException("Thông tin nhập vào không hợp lệ");
        }
        Quiz quiz = objectMapper.convertValue(command, Quiz.class);
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(UpdateQuizCommand updateCommand) {
        Quiz existedQuiz = quizQueryService.getQuizById(updateCommand.getId());

        if (Objects.nonNull(updateCommand.getQuestion())) {
            existedQuiz.setQuestion(updateCommand.getQuestion());
        }

        if (Objects.nonNull(updateCommand.getType())) {
            existedQuiz.setType(updateCommand.getType());
        }

        if (Objects.nonNull(updateCommand.getDifficultlyLevel())) {
            existedQuiz.setDifficultlyLevel(updateCommand.getDifficultlyLevel());
        }

        if (Objects.nonNull(updateCommand.getNumOfCorrectAnswer())) {
            existedQuiz.setNumOfCorrectAnswer(updateCommand.getNumOfCorrectAnswer());
        }

        if (Objects.nonNull(updateCommand.getAnswers())) {
            existedQuiz.setAnswers(updateCommand.getAnswers());
        }

        return quizRepository.save(existedQuiz);
    }

    @Override
    public void deleteQuiz(ObjectId id) {
        quizRepository.deleteById(id);
    }
}
