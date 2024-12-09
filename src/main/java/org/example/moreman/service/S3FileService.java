package org.example.moreman.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface S3FileService {
    String upload(MultipartFile file) throws IOException;

    Map<String, String> delete(String fileName);

    ResponseEntity<InputStreamResource> viewImage(String fileName);

    ResponseEntity<List<String>> listFiles();
}