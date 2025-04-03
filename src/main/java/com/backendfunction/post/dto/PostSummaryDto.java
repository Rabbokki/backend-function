package com.backendfunction.post.dto;

import com.backendfunction.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostSummaryDto {
    private Long id;
    private String title;
    private String content;
    private int price;
    private Long viewCount;
    private int likeCount;
    private int reviewSize;
    private double averageRating;
    private LocalDateTime createdAt;

    private String imgUrl;

    public PostSummaryDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeSize();
        this.reviewSize = post.getReviewSize();
        this.averageRating = post.getAverageRating();
        this.createdAt = post.getCreatedAt();
        this.imgUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getImage();
    }
}
