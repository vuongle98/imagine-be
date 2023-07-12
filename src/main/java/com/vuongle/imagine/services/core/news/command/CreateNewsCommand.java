package com.vuongle.imagine.services.core.news.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateNewsCommand implements Serializable {
    private String title;
    private String content;
}
