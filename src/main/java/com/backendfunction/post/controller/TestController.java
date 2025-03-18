package com.backendfunction.post.controller;

import com.backendfunction.s3.S3Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {
    private final S3Service s3Service;

    public TestController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/test-upload")
    public String testUpload(@RequestPart("file") MultipartFile file) {
        String url = s3Service.uploadFile(file);
        return "Uploaded: " + url;
    }
}