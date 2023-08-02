package com.vuongle.imagine.services.core.quiz.command;

import com.vuongle.imagine.models.Question;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuizCommand extends CreateQuizCommand implements Serializable {

    private ObjectId id;
}
