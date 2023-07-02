package com.vuongle.imagine.services.core.quiz.command;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuestionCommand extends CreateQuestionCommand implements Serializable {
    private ObjectId id;
}
