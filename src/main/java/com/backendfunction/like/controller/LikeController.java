package com.backendfunction.like.controller;

import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/likes/{postId}")
    public ResponseDto<?> postLike(@PathVariable("postId") Long postId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        likeService.postLike(postId,userDetails.getAccount());
        return ResponseDto.success("좋아요 성공");
    }

    @GetMapping("/comment/likes/{commentId}")
    public ResponseDto<?> commentLike(@PathVariable("commentId") Long commentId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        likeService.commentLike(commentId,userDetails.getAccount());
        return ResponseDto.success("좋아요 성공");
    }
}
