package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.dto.crawl.NeedCrawlData;
import com.vuongle.imagine.services.core.crawl.CrawlQuizQTMService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/crawler")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(
        name = "ADMIN - crawl",
        description = "CRUD REST APIs for admin crawl data"
)
public class AdminCrawlController {

    private final CrawlQuizQTMService crawlQuizQTMService;

    public AdminCrawlController(
            CrawlQuizQTMService crawlQuizQTMService
    ) {
        this.crawlQuizQTMService = crawlQuizQTMService;
    }

    @GetMapping("qtm")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<List<NeedCrawlData>> crawlQtm(
            @RequestParam(value = "url") String url
    ) throws IOException {
        List<NeedCrawlData> needCrawlDataList = crawlQuizQTMService.getListNeedCrawlData(url);
        return ResponseEntity.ok(null);
    }

    @GetMapping("qtm/--crawl-and-save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<List<NeedCrawlData>> crawlAndSaveQuiz(
            @RequestParam(value = "url") String url,
            @RequestParam(value = "num-of-page", required = false) Integer numOfPage
    ) throws IOException {
        crawlQuizQTMService.crawlAndSaveQuiz(url, numOfPage);
        return ResponseEntity.ok(null);
    }
}
