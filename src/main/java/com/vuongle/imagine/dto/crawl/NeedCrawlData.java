package com.vuongle.imagine.dto.crawl;

import com.vuongle.imagine.models.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeedCrawlData extends Quiz {
    private String url;
}
