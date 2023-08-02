package com.vuongle.imagine.services.share.storage.impl;

import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.File;
import com.vuongle.imagine.repositories.FileRepository;
import com.vuongle.imagine.services.share.storage.FileQueryService;
import org.bson.types.ObjectId;
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
import java.util.Objects;

@Service
@Transactional
public class FileQueryServiceImpl implements FileQueryService {

    private final FileRepository fileRepository;

    public FileQueryServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Value("${imagine.root.file.path}")
    private String IMAGING_ROOT_FILE_PATH;

    @Override
    public Resource download(String path) throws IOException {
        Path filePath = Paths.get(path);

        if (!Files.exists(filePath)) {
            throw new DataNotFoundException("File not found");
        }

        return new ByteArrayResource(Files.readAllBytes(filePath));
    }

    @Override
    public Resource download(ObjectId fileId) throws IOException {
        File fileInfo = fileRepository.findById(fileId).orElse(null);
        if (Objects.isNull(fileInfo)) {
            throw new DataNotFoundException("File not found");
        }

        return new ByteArrayResource(Files.readAllBytes(Paths.get(fileInfo.getPath())));
    }
}
