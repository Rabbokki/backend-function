package com.backendfunction.review.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import com.backendfunction.review.entity.Review;
import com.backendfunction.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto<?> addOrUpdateReview(Long postId, Account account, int rating, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Optional<Review> existingReview = reviewRepository.findByPostAndAccount(post, account);

        if (existingReview.isPresent()) {
            // Update existing review
            Review review = existingReview.get();
            review.setRating(rating);
            review.setContent(content);
        } else {
            // Create new review
            Review review = new Review(post, account, rating, content);
            reviewRepository.save(review);
            post.setReviewSize(post.getReviewSize() + 1);
        }

        post.recalculateAverageRating();  // Update post rating
        postRepository.save(post);
        return ResponseDto.success("Review added/updated successfully");
    }

    @Transactional
    public ResponseDto<?> removeReview(Long postId, Account account) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Optional<Review> existingReview = reviewRepository.findByPostAndAccount(post, account);

        if (existingReview.isEmpty()) {
            return ResponseDto.fail("100", "Review not found");
        }

        reviewRepository.delete(existingReview.get());
        post.setReviewSize(post.getReviewSize() - 1);
        post.recalculateAverageRating();  // Update post rating
        postRepository.save(post);
        return ResponseDto.success("Review removed successfully");
    }
}