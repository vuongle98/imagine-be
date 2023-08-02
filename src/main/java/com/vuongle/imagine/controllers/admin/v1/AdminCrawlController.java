package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.dto.crawl.NeedCrawlData;
import com.vuongle.imagine.services.core.crawl.CrawlQuizQTMService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/admin/crawler")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminCrawlController {

    private final CrawlQuizQTMService crawlQuizQTMService;

    public AdminCrawlController(
            CrawlQuizQTMService crawlQuizQTMService
    ) {
        this.crawlQuizQTMService = crawlQuizQTMService;
    }

    @GetMapping("qtm")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<NeedCrawlData>> crawlQtm(
            @RequestParam(value = "url") String url
    ) throws IOException {
        List<NeedCrawlData> needCrawlDataList = crawlQuizQTMService.getListNeedCrawlData(url);
        return ResponseEntity.ok(null);
    }

    @GetMapping("qtm/--crawl-and-save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<NeedCrawlData>> crawlAndSaveQuiz(
            @RequestParam(value = "url") String url,
            @RequestParam(value = "num-of-page", required = false) Integer numOfPage
    ) throws IOException {
        crawlQuizQTMService.crawlAndSaveQuiz(url, numOfPage);
        return ResponseEntity.ok(null);
    }
}
