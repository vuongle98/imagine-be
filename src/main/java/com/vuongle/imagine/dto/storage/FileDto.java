package com.vuongle.imagine.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("file")
public class FileDto implements Serializable {

    private ObjectId id;

    private String fileName;

    private String extension;

    private long size;
}
