package com.vuongle.imagine.services.share.storage;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface FileQueryService {

    Resource download(String filePath) throws IOException;
}
