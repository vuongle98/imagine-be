package com.vuongle.imagine.models.embeded;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Answer {

    private String answer;

    private boolean correct;
}
