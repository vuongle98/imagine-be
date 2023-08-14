package com.vuongle.imagine.services.core.quiz.command;

import com.vuongle.imagine.dto.quiz.QuizResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayingQuizHistoryCommand extends CreatePlayingQuizHistoryCommand implements Serializable {

    private ObjectId id;
}
