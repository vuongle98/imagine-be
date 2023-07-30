package com.vuongle.imagine.services.core.storage.impl;

import com.vuongle.imagine.exceptions.StorageException;
import com.vuongle.imagine.models.File;
import com.vuongle.imagine.properties.StorageProperties;
import com.vuongle.imagine.repositories.FileRepository;
import com.vuongle.imagine.services.core.storage.FileService;
import com.vuongle.imagine.utils.StorageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    @Value("${imagine.root.file.path}")
    private String ROOT_UPLOAD_PATH;

    private final Path rootLocation;

    private final FileRepository fileRepository;

    public FileServiceImpl(
            StorageProperties properties,
            FileRepository fileRepository
    ) {
        this.fileRepository = fileRepository;
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public File upload(MultipartFile file) {
        try {
            if (file.isEmpty() || Objects.isNull(file.getOriginalFilename()) || file.getOriginalFilename().isEmpty()) {
                throw new StorageException("File or fileName is empty");
            }

            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();

            Path path = this.rootLocation.resolve(Paths.get(StorageUtils.buildDateFilePath(), file.getOriginalFilename())).normalize();

            if (!path.toString().startsWith(this.rootLocation.toString())) {
                throw new StorageException("Cannot save file outside root directory");
            }

            // copy file to storage
            try (InputStream inputStream = file.getInputStream()) {
                long fileSize = StorageUtils.createFile(inputStream, path);

                // save file info to db
                File fileInfo = new File();
                fileInfo.setFileName(fileName);
                fileInfo.setContentType(contentType);
                fileInfo.setSize(fileSize);
                fileInfo.setPath(path.toString());
                fileInfo.setExtension(StringUtils.getFilenameExtension(path.toString()));

                return fileRepository.save(fileInfo);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public List<File> uploadMultiple(List<MultipartFile> files) {
        return null;
    }


}
