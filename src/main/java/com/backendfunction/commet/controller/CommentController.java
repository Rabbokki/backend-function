package com.backendfunction.commet.controller;

import com.backendfunction.commet.dto.CommentDto;
import com.backendfunction.commet.service.CommentService;
import com.backendfunction.post.dto.PostDto;
import com.backendfunction.post.service.PostService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping({"/api/comment", "/api/comment/"})
    public ResponseEntity<?> commentAll() {
        List<CommentDto> commentDtos = commentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(commentDtos);
    }

    @GetMapping("/api/comment/{id}")
    public ResponseEntity<?> commentSearch(@PathVariable("id") Long id) throws BadRequestException {
        CommentDto findComment = getDto(id, "댓글조회 실패");
        return ResponseEntity.status(HttpStatus.OK).body(findComment);
    }

    @PostMapping("/api/post/{postId}/comment")
    public ResponseEntity<?> commentCreate(
            @PathVariable("postId") Long postId,
            @RequestBody CommentDto dto
    ) throws BadRequestException {
        PostDto post = postService.findByid(postId);
        if (ObjectUtils.isEmpty(post)) {
            throw new BadRequestException("Post 가 없습니다");
        } else {
            commentService.insertComment(dto, postId);
            return ResponseEntity.status(HttpStatus.OK).body("성공");
        }

    }

    @PatchMapping("/api/comment/{id}")
    public ResponseEntity<?> commentUpdate(
            @PathVariable("id") Long id,
            @RequestBody CommentDto dto
    ) throws BadRequestException {
        if (!dto.getId().equals(id)) {
            throw new BadRequestException("댓글 수정 오류");
        }
        commentService.updateByCommentId(dto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<?> commentDelete(@PathVariable("id") Long id) throws BadRequestException {
        CommentDto dto = getDto(id, "댓글 삭제실패");
        commentService.deleteByCommentId(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    private CommentDto getDto(Long id, String message) throws BadRequestException {

        Map<String, Object> findComment = commentService.findByCommentId(id);
        if (ObjectUtils.isEmpty(findComment.get("dto"))) {
            throw new BadRequestException(message);
        }
        return (CommentDto) findComment.get("dto");
    }
}
