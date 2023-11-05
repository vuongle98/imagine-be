package com.vuongle.imagine.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileDto implements Serializable {

    private ObjectId id;

    private String fileName;

    private String extension;

    private long size;
}
