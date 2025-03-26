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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public ResponseDto<?> getReviewsByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        // Retrieve all reviews for this post
        List<Review> reviews = reviewRepository.findByPost(post);
        return ResponseDto.success(reviews);  // Return reviews in the response
    }

    @Transactional
    public ResponseDto<?> addOrUpdateReview(Long postId, Account account, int rating, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Review review = new Review(post, account, rating, content);
        reviewRepository.save(review);

        // Return success with the review as data
        return ResponseDto.success(review);
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