package com.backendfunction.post.controller;

import com.backendfunction.account.entity.Account;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.post.dto.PostReqDto;
import com.backendfunction.post.dto.PostUpReqDto;
import com.backendfunction.post.enums.Category;
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
@RequestMapping("/api/post")
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



    @GetMapping("/find/{id}")
    public ResponseEntity<?> findByPostId(@PathVariable("id") Long id) {
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
    log.info("Received DTO: {}", dto); // DTO 전체 로그 추가
    if (userDetails == null || userDetails.getAccount() == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "로그인이 필요합니다."));
    }
    Account account = userDetails.getAccount();

    log.info("Received DTO: title={}", dto.getTitle());
    log.info("Received image URLs: {}", dto.getImageUrls() != null ? dto.getImageUrls() : "None");
    log.info("Received files: {}", (imgs != null) ? "size=" + imgs.size() : "None");

    try {
        ResponseDto<?> response = postService.createPost(dto, imgs, account);
        return ResponseEntity.ok(response.getData()); // 서비스에서 반환된 데이터를 응답
    } catch (Exception e) {
        log.error("Error creating post", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "게시물 생성 중 오류 발생: " + e.getMessage()));
    }
}


    @GetMapping("/user/posts")
    public ResponseEntity<?> findPostsByUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null || userDetails.getAccount() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Account account = userDetails.getAccount();
        List<PostReqDto> postDtos = postService.findPostsByUser(account);
        return ResponseEntity.status(HttpStatus.OK).body(postDtos);
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
    @GetMapping("/category/{category}")
    public ResponseEntity<?> findByCategory(@PathVariable("category") Category category) {
        List<PostReqDto> post = postService.findByCategory(category);
        System.out.println("Category posts: " + post);
        return ResponseEntity.ok(post);
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



