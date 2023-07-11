package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.services.share.storage.FileQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("file")
@CrossOrigin(origins = "http://localhost:4200")
public class FileServiceController {

    private final FileQueryService fileQueryService;

    @Value("${imagine.root.file.path}")
    private String IMAGINE_ROOT_FILE_PATH;

    public FileServiceController(
            FileQueryService fileQueryService
    ) {
        this.fileQueryService = fileQueryService;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(
            @RequestParam(value = "file-path") String filePath
    ) throws IOException {
        if (Objects.isNull(filePath) || filePath.isBlank() || filePath.isEmpty()) {
            return null;
        }

        if (filePath.startsWith("/") || !filePath.startsWith("images")) {
            return null;
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img.jpg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Resource fileResource = fileQueryService.download(filePath);
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(fileResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileResource);
    }

    @GetMapping("/download-byte")
    public ResponseEntity<byte[]> downloadAsByteArray(
            @RequestParam(value = "file-path") String filePath
    ) throws IOException {
        if (Objects.isNull(filePath) || filePath.isBlank() || filePath.isEmpty()) {
            return null;
        }

        if (filePath.startsWith("/") || !filePath.startsWith(IMAGINE_ROOT_FILE_PATH)) {
            return null;
        }

        Resource fileResource = fileQueryService.download(filePath);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileResource.getContentAsByteArray());
    }
}
