package com.vuongle.imagine.services.core.quiz.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.exceptions.DataFormatException;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.repositories.QuizRepository;
import com.vuongle.imagine.services.core.quiz.QuizService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuizCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuizCommand;
import com.vuongle.imagine.services.share.quiz.QuestionQueryService;
import com.vuongle.imagine.services.share.quiz.QuizQueryService;
import com.vuongle.imagine.services.share.quiz.query.QuestionQuery;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final ObjectMapper objectMapper;

    private final QuizRepository quizRepository;

    private final QuizQueryService quizQueryService;

    private final QuestionQueryService questionQueryService;

    public QuizServiceImpl(
            ObjectMapper objectMapper,
            QuizRepository quizRepository,
            QuizQueryService quizQueryService,
            QuestionQueryService questionQueryService
    ) {
        this.objectMapper = objectMapper;
        this.quizRepository = quizRepository;
        this.quizQueryService = quizQueryService;
        this.questionQueryService = questionQueryService;
    }

    @Override
    public Quiz createQuiz(CreateQuizCommand command) {
        if (!command.isValidateData()) {
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

        if (Objects.nonNull(command.getPublished())) {
            existedQuiz.setPublished(command.getPublished());
        }

        if (Objects.nonNull(command.getMark())) {
            existedQuiz.setMark(command.getMark());
        }

        // check question exist before update
        QuestionQuery questionQuery = new QuestionQuery();
        questionQuery.setListId(command.getListQuestionId());
        List<Question> questions = questionQueryService.findListQuestion(questionQuery);

        if (questions.size() != command.getListQuestionId().size()) {
            throw new DataFormatException("Some questions not found");
        }

        existedQuiz.setListQuestionId(command.getListQuestionId());

        return quizRepository.save(existedQuiz);
    }

    @Override
    public void delete(ObjectId id) {
        quizRepository.deleteById(id);
    }
}
