package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.crawl.NeedCrawlData;
import com.vuongle.imagine.dto.quiz.QuestionResponse;
import com.vuongle.imagine.services.core.crawl.CrawlQuizQTMService;
import com.vuongle.imagine.services.share.quiz.query.QuestionQuery;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.stream.Streams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/crawler")
@CrossOrigin(origins = "http://localhost:4200")
public class CrawlController {

    private final CrawlQuizQTMService crawlQuizQTMService;
    public CrawlController(
            CrawlQuizQTMService crawlQuizQTMService
    ) {
        this.crawlQuizQTMService = crawlQuizQTMService;
    }

    @GetMapping("qtm")
    public ResponseEntity<List<NeedCrawlData>> crawlQtm(
            @RequestParam(value = "url") String url
    ) throws IOException {
        List<NeedCrawlData> needCrawlDataList = crawlQuizQTMService.getListNeedCrawlData(url);
        return ResponseEntity.ok(null);
    }

    @GetMapping("qtm/--crawl-and-save")
    public ResponseEntity<List<NeedCrawlData>> crawlAndSaveQuiz(
            @RequestParam(value = "url") String url,
            @RequestParam(value = "num-of-page", required = false) Integer numOfPage
    ) throws IOException {
        List<NeedCrawlData> needCrawlDataList = crawlQuizQTMService.getListNeedCrawlData(url);
        crawlQuizQTMService.saveQuiz(needCrawlDataList);
        return ResponseEntity.ok(null);
    }
}
