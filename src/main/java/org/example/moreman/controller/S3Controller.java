package com.ORT.controller;

import com.ORT.service.S3FileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/s3")
public class S3Controller {
    private final S3FileService appService;

    public S3Controller(S3FileService appService) {
        this.appService = appService;
    }

    @Operation(
            summary = "List all files in the S3 bucket. Endpoint for ADMIN",
            description = "Retrieves a list of all file names currently stored in the specified S3 bucket.")
    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        return appService.listFiles();
    }

    @Operation(
            summary = "View an image file from S3. Endpoint for ADMIN and USER",
            description = "Retrieves and returns an image file stored in the S3 bucket based on the provided file name.")
    @GetMapping("image/{fileName}")
    public ResponseEntity<InputStreamResource> viewImage(@PathVariable String fileName) {
        return appService.viewImage(fileName);
    }

    @Operation(
            summary = "Upload a file to S3. Endpoint for ADMIN",
            description = "Uploads a provided file to the S3 bucket. The file is stored with a unique name generated using the current timestamp.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        return appService.upload(file);
    }

    @Operation(
            summary = "Delete a file from S3. Endpoint for ADMIN",
            description = "Deletes a specified file from the S3 bucket based on the provided file name.")
    @DeleteMapping("/delete/{fileName}")
    public Map<String, String> delete(@PathVariable String fileName) {
        return appService.delete(fileName);
    }
}