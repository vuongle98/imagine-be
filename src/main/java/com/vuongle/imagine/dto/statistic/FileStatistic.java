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
public class FileStatistic extends Statistic implements Serializable {

    private float totalSize;
    private float totalImage;
    private float totalVideo;
    private float totalAudio;
    private float totalDocument;
//    private float largestSize;
//    private float smallestSize;
//    private float averageSize;
//    private List<String> extensions;
}
