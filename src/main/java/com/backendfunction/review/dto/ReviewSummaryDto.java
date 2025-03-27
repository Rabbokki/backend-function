package com.backendfunction.review.dto;

import com.backendfunction.review.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewSummaryDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    public ReviewSummaryDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}
