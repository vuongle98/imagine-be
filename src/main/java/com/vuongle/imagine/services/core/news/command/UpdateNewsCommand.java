package com.vuongle.imagine.services.core.news.command;

import lombok.*;
import org.bson.types.ObjectId;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateNewsCommand extends CreateNewsCommand implements Serializable {

    private ObjectId id;
}
