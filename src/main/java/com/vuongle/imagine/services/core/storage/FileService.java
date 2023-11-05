package com.vuongle.imagine.services.core.storage;

import com.vuongle.imagine.models.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    File upload(MultipartFile file);

    File internalUpload(String url, String fileName) throws IOException;

    List<File> uploadMultiple(List<MultipartFile> files);
}
