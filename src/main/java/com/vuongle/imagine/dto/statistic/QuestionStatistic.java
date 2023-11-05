package com.vuongle.imagine.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionStatistic extends Statistic implements Serializable {

    private float totalHard;
    private float totalMedium;
    private float totalEasy;
    private float totalHasFile;
    private float totalHasCode;
}
