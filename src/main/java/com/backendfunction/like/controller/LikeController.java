package com.backendfunction.like.controller;

import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.like.dto.PostLikeDto;
import com.backendfunction.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/likes")
@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final AccountRepository accountRepository;

    @PostMapping("/{postId}")
    public ResponseDto<?> addPostLike(@PathVariable("postId") Long postId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.addPostLike(postId, userDetails.getAccount());
    }

    @DeleteMapping("/{postId}")
    public ResponseDto<?> removePostLike(@PathVariable("postId") Long postId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.removePostLike(postId, userDetails.getAccount());
    }

    @GetMapping("/status/{postId}")
    public ResponseDto<?> getPostLikeStatus(@PathVariable("postId") Long postId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean isLiked = likeService.isPostLiked(postId, userDetails.getAccount());
        return ResponseDto.success(isLiked);
    }

    @GetMapping("/comment/likes/{commentId}")
    public ResponseDto<?> commentLike(@PathVariable("commentId") Long commentId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        likeService.commentLike(commentId,userDetails.getAccount());
        return ResponseDto.success("좋아요 성공");
    }
}
