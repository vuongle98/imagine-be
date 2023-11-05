package com.vuongle.imagine.models;

import com.vuongle.imagine.constants.QuizCategory;
import com.vuongle.imagine.constants.QuizLevel;
import com.vuongle.imagine.dto.crawl.NeedCrawlData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Document("quiz")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Quiz implements Serializable {

    @Id
    @NotNull
    private ObjectId id;

    @NotNull
    private List<ObjectId> listQuestionId;

    private List<Question> questions;

    @Size(min = 2, max = 100, message = "The title must be between 2 and 100 messages.")
    @NotNull
    private String title;

    @Size(max = 500, message = "The description can't be longer than 500 characters.")
    @NotNull
    private String description;

    private String imagePath;

    private File image;

    private boolean published;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

    @CreatedBy
    private String createdBy;

    @NotNull
    private Integer countDown;

    @NotNull
    private QuizCategory category;

    @NotNull
    private QuizLevel level;

    private boolean mark;

    public Quiz(NeedCrawlData needCrawlData) {
        this.title = needCrawlData.getTitle();
        this.description = needCrawlData.getDescription();
        this.listQuestionId = needCrawlData.getListQuestionId();
        this.published = needCrawlData.isPublished();
        this.imagePath = needCrawlData.getImagePath();
        this.image = needCrawlData.getImage();
    }
}
