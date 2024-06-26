package com.vuongle.imagine.services.core.quiz.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuongle.imagine.constants.QuestionType;
import com.vuongle.imagine.exceptions.DataFormatException;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.models.embeded.Answer;
import com.vuongle.imagine.repositories.QuestionRepository;
import com.vuongle.imagine.services.core.quiz.QuestionService;
import com.vuongle.imagine.services.core.quiz.command.CreateQuestionCommand;
import com.vuongle.imagine.services.core.quiz.command.UpdateQuestionCommand;
import com.vuongle.imagine.services.share.quiz.QuestionQueryService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionQueryService questionQueryService;

    private final ObjectMapper objectMapper;

    public QuestionServiceImpl(
            QuestionRepository questionRepository,
            ObjectMapper objectMapper,
            QuestionQueryService questionQueryService
    ) {
        this.questionRepository = questionRepository;
        this.objectMapper = objectMapper;
        this.questionQueryService = questionQueryService;
    }

    @Override
    public Question createQuestion(CreateQuestionCommand command) {
        if (!command.validateQuestion()) {
            throw new DataFormatException("Thông tin nhập vào không hợp lệ");
        }
        Question question = objectMapper.convertValue(command, Question.class);

        List<Answer> correctAnswer = new ArrayList<>();

        for (Answer answer : command.getAnswers()) {
            if (answer.isCorrect()) {
                correctAnswer.add(answer);
            }
        }

        question.setCorrectAnswer(correctAnswer);

        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(UpdateQuestionCommand updateCommand) {

        if (!updateCommand.validateQuestion()) {
            throw new DataFormatException("Data not valid");
        }

        Question existedQuestion = questionQueryService.getQuestionById(updateCommand.getId());

        if (Objects.equals(updateCommand.getType(), QuestionType.YES_NO) && updateCommand.getAnswers().stream().filter(Answer::isCorrect).count() > 1) {
            throw new DataFormatException("Câu hỏi yes/no chỉ có 1 câu trả lời đúng");
        }

        if (Objects.nonNull(updateCommand.getTitle())) {
            existedQuestion.setTitle(updateCommand.getTitle());
        }

        if (Objects.nonNull(updateCommand.getType())) {
            existedQuestion.setType(updateCommand.getType());
        }

        if (Objects.nonNull(updateCommand.getDifficultlyLevel())) {
            existedQuestion.setDifficultlyLevel(updateCommand.getDifficultlyLevel());
        }

        if (Objects.nonNull(updateCommand.getAnswers())) {
            existedQuestion.setAnswers(updateCommand.getAnswers());
        }

        if (Objects.nonNull(updateCommand.getCategory())) {
            existedQuestion.setCategory(updateCommand.getCategory());
        }

        if (Objects.nonNull(updateCommand.getCountDown())) {
            existedQuestion.setCountDown(updateCommand.getCountDown());
        }

        if (Objects.nonNull(updateCommand.getMark())) {
            existedQuestion.setMark(updateCommand.getMark());
        }

        // call update all quiz

        return questionRepository.save(existedQuestion);
    }

    @Override
    public void deleteQuestion(ObjectId id) {
        // delete from quiz
        questionRepository.deleteById(id);
    }
}
