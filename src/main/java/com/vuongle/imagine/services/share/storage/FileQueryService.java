package com.vuongle.imagine.services.share.storage;

import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.storage.query.FileQuery;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface FileQueryService extends BaseService<FileQuery> {

    Resource download(String filePath) throws IOException;

    Resource download(ObjectId fileId) throws IOException;
}
