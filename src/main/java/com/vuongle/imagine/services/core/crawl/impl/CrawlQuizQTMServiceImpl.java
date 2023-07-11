package com.vuongle.imagine.services.core.crawl.impl;

import com.vuongle.imagine.dto.crawl.NeedCrawlData;
import com.vuongle.imagine.models.Question;
import com.vuongle.imagine.models.Quiz;
import com.vuongle.imagine.models.embeded.Answer;
import com.vuongle.imagine.repositories.QuestionRepository;
import com.vuongle.imagine.repositories.QuizRepository;
import com.vuongle.imagine.services.core.crawl.CrawlQuizQTMService;
import com.vuongle.imagine.services.core.quiz.QuizService;
import com.vuongle.imagine.utils.StorageUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CrawlQuizQTMServiceImpl implements CrawlQuizQTMService {

    private final QuizService quizService;

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final String STORE_IMAGE_PATH = "images";

    @Value("${imagine.root.file.path}")
    private String IMAGING_ROOT_FILE_PATH;

    public CrawlQuizQTMServiceImpl(
        QuizService quizService,
        QuizRepository quizRepository,
        QuestionRepository questionRepository
    ) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<NeedCrawlData> getListNeedCrawlData(String url) throws IOException {
        List<NeedCrawlData> needCrawlDataList = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements elements = document.getElementsByClass("listitem");
        for (Element element: elements) {
            NeedCrawlData needCrawlData = new NeedCrawlData();
            Elements headInfo = element.getElementsByTag("h3");
            if (Objects.nonNull(headInfo.first())) {
                Element aTag = Objects.requireNonNull(headInfo.first()).getElementsByTag("a").first();

                if (Objects.nonNull(aTag)) {
                    needCrawlData.setUrl(aTag.absUrl("href"));
                    needCrawlData.setTitle(aTag.text());
                }
            }

            Element desc = element.getElementsByClass("desc").first();
            if (Objects.nonNull(desc)) {
                needCrawlData.setDescription(desc.text());
            }

            Element thumb = element.getElementsByClass("thumb").first();

            if (Objects.nonNull(thumb)) {

                Element img = thumb.getElementsByTag("img").first();

                if (Objects.nonNull(img)) {
                    String imgUrl = img.absUrl("data-src");
                    if (imgUrl.isEmpty() || imgUrl.isBlank()) {
                        imgUrl = img.absUrl("src");
                    }

                    List<String> splitUrl = Arrays.asList(imgUrl.split("\\."));

                    String ext = splitUrl.get(splitUrl.size() - 1);

                    String imageName = StorageUtils.buildPathFromName(needCrawlData.getTitle(), ext);
                    System.out.println(imageName);
                    try (InputStream in = new URL(imgUrl).openStream()) {
                        Path imagePath = Paths.get(IMAGING_ROOT_FILE_PATH, STORE_IMAGE_PATH, StorageUtils.buildDateFilePath(), imageName);
                        StorageUtils.createFile(in, imagePath);
                        needCrawlData.setImagePath(imagePath.toString());
                    }
                }
            }

            needCrawlDataList.add(needCrawlData);
        }

        return needCrawlDataList;
    }

    @Override
    public List<Question> crawlQuestion(NeedCrawlData needCrawlData) throws IOException {
        List<Question> questions = new ArrayList<>();
        Document document = Jsoup.connect(needCrawlData.getUrl()).get();
        Elements elements = document.getElementsByClass("quiz-section");
        for (Element element: elements) {
            Question question = new Question();
            List<Answer> answers = new ArrayList<>();
            Element titleNode = element.getElementsByClass("quiz-section-title").first();
            Element contentNode = element.getElementsByClass("quiz-section-content").first();

            if (Objects.nonNull(titleNode)) {
                question.setTitle(titleNode.text());
            }

            List<Answer> correctAnswers = new ArrayList<>();

            if (Objects.nonNull(contentNode)) {

                Elements questionDesc = contentNode.getElementsByClass("item-desc");

                if (Objects.nonNull(questionDesc.first())) {
                    Element preTag = questionDesc.first().getElementsByTag("pre").first();

                    if (Objects.nonNull(preTag)) {
                        question.setDescription(preTag.toString());
                    }
                }

                Elements answerNodes = contentNode.getElementsByClass("list-item");

                for (Element answerItem: answerNodes) {
                    Answer answer = new Answer();
                    Element answerDesc = answerItem.getElementsByClass("answer-desc").first();

                    if (Objects.nonNull(answerDesc)) {
                        answer.setAnswer(answerDesc.text());
                    }

                    Element correctAnswerNode = answerItem.getElementsByTag("input").first();

                    if (Objects.nonNull(correctAnswerNode)) {
                        answer.setCorrect(Integer.parseInt(correctAnswerNode.attr("data-status")) == 1);
                    } else {
                        answer.setCorrect(false);
                    }

                    if (answer.isCorrect()) {
                        correctAnswers.add(answer);
                    }

                    answers.add(answer);
                }
            }

            question.setAnswers(answers);
            question.setCorrectAnswer(correctAnswers);
            questions.add(question);
            System.out.println("ok");
        }
        return questions;
    }

    @Override
    public void saveQuiz(List<NeedCrawlData> needCrawlDataList) throws IOException {
        for (NeedCrawlData needCrawlData: needCrawlDataList) {
            Quiz quiz = new Quiz(needCrawlData);
            List<Question> questions = crawlQuestion(needCrawlData);
            questions = this.questionRepository.saveAll(questions);
            quiz.setListQuestionId(questions.stream().map(Question::getId).collect(Collectors.toList()));
            quizRepository.save(quiz);
        }
    }
}
