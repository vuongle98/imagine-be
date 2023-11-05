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
public class UserStatistic extends Statistic implements Serializable {

    private Integer totalAdmin;
    private Integer totalUser;
    private Integer totalModerator;
}
