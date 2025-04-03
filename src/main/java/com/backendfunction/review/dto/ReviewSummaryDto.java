package com.backendfunction.review.dto;

import com.backendfunction.review.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewSummaryDto {
    private Long id;
    private int rating;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    private String profilePic;

    public ReviewSummaryDto(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.nickname = review.getNickname();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.profilePic = (review.getAccount().getImgUrl() == null || review.getAccount().getImgUrl().isEmpty())
                ? null
                : review.getAccount().getImgUrl();
    }
}
