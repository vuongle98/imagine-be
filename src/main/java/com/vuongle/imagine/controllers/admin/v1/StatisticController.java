package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.dto.statistic.*;
import com.vuongle.imagine.services.share.statistic.impl.StatisticServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistic")
@Tag(
        name = "ADMIN - statistic",
        description = "CRUD REST APIs for admin statistic data"
)
public class StatisticController {

    private final StatisticServiceImpl statisticService;


    public StatisticController(StatisticServiceImpl statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Object> adminStatisticUser(

    ) {
        FullStatistic statistic = statisticService.statistic();
        UserStatistic userStatistic = new UserStatistic();
        userStatistic.setTotalUser(900);
        userStatistic.setTotalAdmin(120);
        userStatistic.setTotalRows(1100);
        userStatistic.setTotalModerator(400);

        statistic.setUserStatistic(userStatistic);

        QuizStatistic quizStatistic = new QuizStatistic();
        quizStatistic.setTotalRows(900);
        quizStatistic.setTotalEasy(200);
        quizStatistic.setTotalMedium(300);
        quizStatistic.setTotalHard(100);

        statistic.setQuizStatistic(quizStatistic);

        QuestionStatistic questionStatistic = new QuestionStatistic();
        questionStatistic.setTotalRows(1200);
        questionStatistic.setTotalEasy(900);
        questionStatistic.setTotalMedium(790);
        questionStatistic.setTotalHard(100);
        questionStatistic.setTotalHasCode(100);
        questionStatistic.setTotalHasFile(40);

        statistic.setQuestionStatistic(questionStatistic);

        FileStatistic fileStatistic = new FileStatistic();
        fileStatistic.setTotalRows(1000);
        fileStatistic.setTotalImage(200);
        fileStatistic.setTotalVideo(400);
        fileStatistic.setTotalAudio(100);
        fileStatistic.setTotalDocument(600);

//        fileStatistic.setAverageSize(2000);
//        fileStatistic.setExtensions(List.of("pdf", "doc", "docx"));
//        fileStatistic.setLargestSize(100000);
//        fileStatistic.setSmallestSize(1000);

        statistic.setFileStatistic(fileStatistic);

        return ResponseEntity.ok(statistic);
    }

}
