package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.models.File;
import com.vuongle.imagine.services.core.storage.FileService;
import com.vuongle.imagine.services.share.storage.FileQueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
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
@RequestMapping("/api/file")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@Tag(
        name = "File",
        description = "CRUD REST APIs for file service"
)
public class FileServiceController {

    private final FileQueryService fileQueryService;

    private final FileService fileService;

    @Value("${imagine.root.file.path}")
    private String IMAGINE_ROOT_FILE_PATH;

    public FileServiceController(
            FileQueryService fileQueryService,
            FileService fileService
    ) {
        this.fileQueryService = fileQueryService;
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<File> upload(
            @RequestParam(value = "file")MultipartFile file
            ) {
        File fileInfo = this.fileService.upload(file);
        return ResponseEntity.ok(fileInfo);
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    public ResponseEntity<Object> download(
            @PathVariable(value = "id") ObjectId id,
            @RequestParam(value = "get-byte", required = false) boolean getByte
    ) throws IOException {
        if (Objects.isNull(id)) {
            return null;
        }
        Resource fileResource = fileQueryService.download(id);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img.jpg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        if (getByte) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(fileResource.getContentAsByteArray());
        }

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(fileResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileResource);
    }

    @GetMapping("/download")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MODERATOR')")
    @SecurityRequirement(
            name = "Bearer authentication"
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
