package com.vuongle.imagine.services.core.quiz.command;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuizCommand extends CreateQuizCommand {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
}
