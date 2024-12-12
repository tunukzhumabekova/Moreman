package org.example.moreman.service.impl;

import org.example.moreman.service.S3FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class S3FileServiceImpl implements S3FileService {
    private final S3Client s3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private final Logger LOGGER = LoggerFactory.getLogger(S3FileServiceImpl.class);

    public S3FileServiceImpl(S3Client s3) {
        this.s3 = s3;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        LOGGER.info("Starting file upload: {}", file.getOriginalFilename());
        String key = System.currentTimeMillis() + file.getOriginalFilename();
        try {
            PutObjectRequest por = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3.putObject(por, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (Exception e) {
            LOGGER.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw e;
        }
        LOGGER.info("Upload complete.");
        return key;
    }

    @Override
    public Map<String, String> delete(String fileName) {
        LOGGER.info("Attempting to delete file: {}", fileName);
        try {
            LOGGER.info("File deleted successfully: {}", fileName);
            s3.deleteObject(dor -> dor.bucket(bucketName).key(fileName).build());
        } catch (S3Exception e) {
            LOGGER.error("Failed to delete file from S3: {}. AWS error: {}",
                    fileName, e.awsErrorDetails().errorMessage());
            throw new IllegalStateException(e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred during file deletion: {}", fileName, e);
            throw new IllegalStateException(e.getMessage());
        }
        return Map.of(
                "message", fileName + " has been deleted."
        );
    }

    @Override
    public ResponseEntity<InputStreamResource> viewImage(String filename) {
        LOGGER.info("Retrieving image file: {}", filename);
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();
            InputStream fileStream = s3.getObject(getObjectRequest, ResponseTransformer.toInputStream());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            LOGGER.info("Successfully retrieved image: {}", filename);
            return new ResponseEntity<>(new InputStreamResource(fileStream), headers, HttpStatus.OK);
        } catch (S3Exception e) {
            LOGGER.error("Failed to retrieve image from S3: {}", filename, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<String>> listFiles() {
        LOGGER.info("Listing files in bucket: {}", bucketName);
        try {
            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsV2Response listObjectsResponse = s3.listObjectsV2(listObjectsRequest);
            List<String> fileNames = listObjectsResponse.contents().stream()
                    .map(S3Object::key)
                    .collect(Collectors.toList());

            LOGGER.info("Successfully listed files in bucket: {}", bucketName);
            return ResponseEntity.ok(fileNames);
        } catch (S3Exception e) {
            LOGGER.error("Failed to list files in bucket: {}", bucketName, e);
            throw e;
        }
    }
}