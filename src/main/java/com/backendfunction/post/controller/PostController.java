package com.backendfunction.post.controller;

import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.post.dto.PostDto;
import com.backendfunction.post.service.PostService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping({"" ,"/"})
    public ResponseEntity<?> findAllPost() {
        List<PostDto> postDtos = postService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(postDtos);
    }
//    @GetMapping("/id/{id}")
//    public ResponseEntity<?> findByPostId(@PathVariable("id") Long id) throws BadRequestException {
//
////        PostDto findPost = getDto(id, "Post 조회 실패");
////        return ResponseEntity.status(HttpStatus.OK).body(findPost);
//    }
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestParam(value = "postImg", required = false) List<MultipartFile> imgs,
                                        @RequestPart(value = "dto") PostDto dto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.createPost(dto, imgs, userDetails.getAccount());
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }
//수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@RequestBody PostDto dto, @PathVariable("id") Long id) throws BadRequestException {
        if (!dto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("실패");
        }
//        PostDto postDto = getDto(id, "Post 수정 실패");
//        postService.updateByPost(dto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) throws BadRequestException {
//        PostDto result = getDto(id, "Post 삭제 실패");
//        postService.deleteByPostId(result.getId());
        return ResponseEntity.status(HttpStatus.OK).body("성공");

    }
    //    카테고리별 출력
//    @GetMapping("/category/{category}")
//    public ResponseEntity<?> findCategory(@PathVariable("category") Category category) {
//        List<PostDto> postDtos= postService.findByCategory(category);
//        return ResponseEntity.status(HttpStatus.OK).body(postDtos);
//    }
//    private PostDto getDto(Long id, String message) throws BadRequestException {
//
//        Map<String, Object> findPost = postService.findByPostId(id);
//        if (ObjectUtils.isEmpty(findPost.get("dto"))) {
//            throw new BadRequestException(message);
//        }
//        return (PostDto) findPost.get("dto");
//    }
}



