package com.backendfunction.post.controller;

import com.backendfunction.account.entity.Account;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.post.dto.PostReqDto;
import com.backendfunction.post.dto.PostUpReqDto;
import com.backendfunction.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Slf4j
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping({"" ,"/"})
    public ResponseEntity<?> findAllPost() {
        List<PostReqDto> postDtos = postService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(postDtos);
    }

<<<<<<< HEAD
=======
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
=======
<<<<<<< HEAD

=======
>>>>>>> feature-joo-test-chat
>>>>>>> 45c8ce3e2758eb9c3280d93cf3bf090750461836
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findByPostId(@PathVariable("id") Long id) {
>>>>>>> b4e06d9ba474ab77e5570422e6d2765e1f485a1c
        PostReqDto dto = postService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
//    @GetMapping("/id/{id}")
//    public ResponseEntity<?> findByPostId(@PathVariable("id") Long id) throws BadRequestException {
//
////        PostDto findPost = getDto(id, "Post 조회 실패");
////        return ResponseEntity.status(HttpStatus.OK).body(findPost);
//    }
@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> createPost(
        @RequestPart(value = "postImg", required = false) List<MultipartFile> imgs,
        @RequestPart(value = "dto") PostReqDto dto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
    log.info("Received DTO: title={}, imageUrls={}", dto.getTitle(), dto.getImageUrls());
    log.info("Received files: {}", imgs != null ? "size=" + imgs.size() : "null");
    if (userDetails == null || userDetails.getAccount() == null) {
        log.error("Account is required but userDetails is null");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("게시물 생성을 위해 로그인이 필요합니다.");
    }
    Account account = userDetails.getAccount();
    postService.createPost(dto, imgs, account);
    return ResponseEntity.status(HttpStatus.OK).body("성공");
}
    //글 삭제 fix
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws BadRequestException {
        postService.deleteByPostId(id,userDetails.getAccount());
        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");

    }
//@PostMapping("/create")
//public ResponseEntity<?> createPost(@RequestParam(value = "postImg", required = false) List<MultipartFile> imgs,
//                                    @RequestPart(value = "dto") PostDto dto,
//                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
//    log.info("Received createPost request with imgs: " + (imgs != null ? imgs.size() : 0));
//    postService.createPost(dto, imgs, userDetails.getAccount());
//    return ResponseEntity.status(HttpStatus.OK).body("성공");
//}
//수정

    @PatchMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable("id") Long id,
            @RequestParam(value = "postImg", required = false) List<MultipartFile> imgs,
            @RequestPart(value = "dto") PostUpReqDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request) {
        log.info("Content-Type received: {}", request.getContentType());
        log.info("Received update request for post ID: {}, dto: {}, imgs: {}", id, dto, imgs != null ? imgs.size() : 0);
        try {
            ResponseDto<?> response = postService.updateByPost(id, imgs, dto, userDetails.getAccount());
            if (!response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response.getError());
            }
            return ResponseEntity.status(HttpStatus.OK).body("업데이트 성공");
        } catch (Exception e) {
            log.error("Error updating post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트 실패");
        }
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



