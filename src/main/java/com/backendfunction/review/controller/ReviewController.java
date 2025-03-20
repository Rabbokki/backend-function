package com.backendfunction.review.controller;

import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    //
    @PostMapping("/{postId}")
    public ResponseDto<?> addOrUpdateReview(@PathVariable("postId") Long postId,
                                            @RequestParam("rating") int rating,
                                            @RequestParam("content") String content,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.addOrUpdateReview(postId, userDetails.getAccount(), rating, content);
    }

    //
    @DeleteMapping("/{postId}")
    public ResponseDto<?> removeReview(@PathVariable("postId") Long postId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.removeReview(postId, userDetails.getAccount());
    }
}