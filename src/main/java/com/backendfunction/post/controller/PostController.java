package com.backendfunction.post.controller;

<<<<<<< HEAD
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
@RequestMapping("/api/post")
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
    @GetMapping("/{id}")
    public ResponseEntity<?> findByPostId(@PathVariable("id") Long id) throws BadRequestException {

        PostDto findPost = getDto(id, "Post 조회 실패");
        return ResponseEntity.status(HttpStatus.OK).body(findPost);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostDto dto) {
        postService.createPost(dto);

        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }
//수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@RequestBody PostDto dto, @PathVariable("id") Long id) throws BadRequestException {
        if (!dto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("실패");
        }
        PostDto postDto = getDto(id, "Post 수정 실패");
        postService.updateByPost(dto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) throws BadRequestException {
        PostDto result = getDto(id, "Post 삭제 실패");
        postService.deleteByPostId(result.getId());
        return ResponseEntity.status(HttpStatus.OK).body("성공");

    }
    private PostDto getDto(Long id, String message) throws BadRequestException {

        Map<String, Object> findPost = postService.findByPostId(id);
        if (ObjectUtils.isEmpty(findPost.get("dto"))) {
            throw new BadRequestException(message);
        }
        return (PostDto) findPost.get("dto");
    }

=======
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
>>>>>>> develop
}
