package com.vuongle.imagine.services.core.quiz.command;

import lombok.*;
import org.bson.types.ObjectId;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuizCommand extends CreateQuizCommand implements Serializable {

    private ObjectId id;
}
