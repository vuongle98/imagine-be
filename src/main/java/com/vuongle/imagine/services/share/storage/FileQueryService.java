package com.vuongle.imagine.services.share.storage;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface FileQueryService {

    Resource download(String filePath) throws IOException;

    Resource download(ObjectId fileId) throws IOException;
}
