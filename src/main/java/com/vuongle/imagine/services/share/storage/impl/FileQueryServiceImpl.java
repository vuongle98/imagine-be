package com.vuongle.imagine.services.share.storage.impl;

import com.vuongle.imagine.services.share.storage.FileQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class FileQueryServiceImpl implements FileQueryService {

    @Value("${imagine.root.file.path}")
    private String IMAGING_ROOT_FILE_PATH;

    @Override
    public Resource download(String path) throws IOException {
        Path filePath = Paths.get(path);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found");
        }

        return new ByteArrayResource(Files.readAllBytes(filePath));
    }
}
