package com.vuongle.imagine.dto.statistic;

import com.vuongle.imagine.constants.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConversationStatistic extends Statistic implements Serializable {
    private float largestMember;
    private float smallestMember;
    private List<ChatType> chatTypes;
}
