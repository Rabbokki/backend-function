package com.backendfunction.post.dto;

import com.backendfunction.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostSummaryDto {
    private Long id;
    private String title;
    private int price;

    private String imgUrl;

    public PostSummaryDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.price = post.getPrice();
        this.imgUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getImage();
    }
}
