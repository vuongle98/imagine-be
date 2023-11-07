package com.vuongle.imagine.services.share.storage.query;

import com.vuongle.imagine.constants.CommonFileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileQuery implements Serializable {

    private ObjectId id;

    private CommonFileType type;

    private String likeName;

    private String checksum;
}
