package com.backendfunction.commet.controller;

import com.backendfunction.commet.dto.CommentDto;
import com.backendfunction.commet.dto.CommentReqDto;
import com.backendfunction.commet.service.CommentService;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @GetMapping({"/"})
    public ResponseEntity<?> commentAll() {
        commentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body("전체 조회");
    }

//    @GetMapping("/api/comment/{id}")
//    public ResponseEntity<?> commentSearch(@PathVariable("id") Long id) throws BadRequestException {
//        CommentDto findComment = getDto(id, "댓글조회 실패");
//        return ResponseEntity.status(HttpStatus.OK).body(findComment);
//    }

    @PostMapping("/post/{postId}/comment")
    public ResponseDto<?> commentCreate(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CommentReqDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) throws BadRequestException {

        return commentService.insertComment(dto,postId, userDetails.getAccount());
    }

    @PatchMapping("/{id}")
    public ResponseDto<?> commentUpdate(
            @PathVariable("id") Long id,
            @RequestBody CommentReqDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws BadRequestException {
        return commentService.updateByCommentId(id,dto,userDetails.getAccount());
    }

    @DeleteMapping("/{id}")
    public ResponseDto<?> commentDelete(@PathVariable("id") Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails
                ) throws BadRequestException {
        return commentService.deleteByCommentId(id,userDetails.getAccount());
    }

}
