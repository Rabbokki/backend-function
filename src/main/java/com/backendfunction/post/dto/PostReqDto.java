package com.backendfunction.post.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDto {
    private String title;
    private String content;
    private int price;
    private MultipartFile img;
}
