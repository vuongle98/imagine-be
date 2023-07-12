package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.dto.crawl.NeedCrawlData;
import com.vuongle.imagine.services.core.crawl.CrawlQuizQTMService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
        List<NeedCrawlData> needCrawlDataList = crawlQuizQTMService.getListNeedCrawlData(url);
        crawlQuizQTMService.saveQuiz(needCrawlDataList);
        return ResponseEntity.ok(null);
    }
}
