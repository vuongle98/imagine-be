package com.vuongle.imagine.services.core.storage;

import com.vuongle.imagine.models.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File upload(MultipartFile file);

    List<File> uploadMultiple(List<MultipartFile> files);
}
