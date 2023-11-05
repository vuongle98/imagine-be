package com.vuongle.imagine.controllers.admin.v1;

import com.vuongle.imagine.models.File;
import com.vuongle.imagine.services.core.storage.FileService;
import com.vuongle.imagine.services.share.storage.FileQueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin/file")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@Tag(
        name = "ADMIN - file",
        description = "CRUD REST APIs for admin manage file"
)
public class AdminFileServiceController {

    private final FileQueryService fileQueryService;

    private final FileService fileService;

    @Value("${imagine.root.file.path}")
    private String IMAGINE_ROOT_FILE_PATH;

    public AdminFileServiceController(
            FileQueryService fileQueryService,
            FileService fileService
    ) {
        this.fileQueryService = fileQueryService;
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<File> upload(
            @RequestParam(value = "file")MultipartFile file
            ) {
        File fileInfo = this.fileService.upload(file);
        return ResponseEntity.ok(fileInfo);
    }

    @GetMapping("/download")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @Tag(
            name = "ADMIN - file",
            description = "CRUD REST APIs for admin manage file"
    )
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
