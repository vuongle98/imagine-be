package com.vuongle.imagine.models.embeded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer extends BaseAnswer implements Serializable {

    private boolean correct;

    private ObjectId id = new ObjectId();
}
