package com.backendfunction.review.dto;

import com.backendfunction.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long postId;
    private Long accountId;
    private int rating;
    private String content;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.postId = review.getPost().getId();
        this.accountId = review.getAccount().getId();
        this.rating = review.getRating();
        this.content = review.getContent();
    }
}
