package com.backendfunction.post.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.image.entity.Image;
import com.backendfunction.global.image.repository.ImageRepository;
import com.backendfunction.post.dto.PostReqDto;
import com.backendfunction.post.dto.PostUpReqDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import com.backendfunction.s3.S3Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public ResponseDto<?> createPost(PostReqDto dto, List<MultipartFile> file, Account account) {
        log.info("Creating post for account: {}", account != null ? account.getId() : "null");
        log.info("Creating post with category: {}", dto.getCategory());
        Post post = new Post(dto, account);
        post = postRepository.save(post);
        log.info("Post saved with ID: {}", post.getId());

        List<String> imageUrls = dto.getImageUrls();
        log.info("Image URLs from DTO: {}", imageUrls);
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String url : imageUrls) {
                log.info("Adding image URL: {}", url);
                Image image = new Image(url, post);
                post.getImages().add(image);
                imageRepository.save(image);
            }
        } else {
            log.info("No image URLs provided in DTO");
        }

        log.info("File parameter received: {}", file != null ? "size=" + file.size() : "null");
        if (file != null && !file.isEmpty()) {
            log.info("Processing {} files", file.size());
            for (MultipartFile multipartFile : file) {
                log.info("Processing file: name={}, size={}",
                        multipartFile.getOriginalFilename(), multipartFile.getSize());
                String s3Url = s3Service.uploadFile(multipartFile);
                log.info("S3 URL generated: {}", s3Url);
                Image image = new Image(s3Url, post);
                post.getImages().add(image);
                imageRepository.save(image);
                log.info("Image saved with ID: {}", image.getId());
            }
        } else {
            log.info("No files provided for upload");
        }

        postRepository.save(post);
        PostReqDto postDto = new PostReqDto(post);
        log.info("Returning post DTO with image count: {}", post.getImages().size());
        return ResponseDto.success(postDto);
    }

    public ResponseDto<?> updateByPost(Long id, List<MultipartFile> imgs, PostUpReqDto dto, Account account) {
        Post post = postRepository.findPostByIdAndAccount(id, account);
        if (post == null) return ResponseDto.fail("600", "수정 권한이 없습니다.");

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPrice(dto.getPrice());

        if (imgs != null && !imgs.isEmpty()) {
            List<Image> imageList = post.getImages();
            imageList.clear();
            for (MultipartFile img : imgs) {
                String imgUrl = s3Service.uploadFile(img);
                Image image = new Image(imgUrl, post);
                imageList.add(image);
            }
            post.setImages(imageList);
        }
        postRepository.save(post);

        return ResponseDto.success("업데이트 성공~");
    }

    @Transactional
    public ResponseDto<?> deleteByPostId(Long id, Account account) {
        Post post = postRepository.findPostByIdAndAccount(id, account);
        if (post == null) return ResponseDto.fail("600", "삭제 권한이 없습니다.");
        postRepository.delete(post);
        return ResponseDto.success("삭제 완료");
    }

    @Transactional(readOnly = true)
    public List<PostReqDto> findAll() {
        List<Post> posts = postRepository.findAll();
        log.info("Posts:", posts);

        // Recalculate averageRating and reviewSize for each post
        posts.forEach(post -> {
            post.recalculateAverageRating();
            post.setReviewSize(post.getReviews().size()); // Set reviewSize
        });

        return posts.stream()
                .map(PostReqDto::fromEntity) // Convert Post to PostReqDto
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public PostReqDto findById(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (ObjectUtils.isEmpty(post)) {
            return null;
        }
        return PostReqDto.fromEntity(post);
    }

    @Transactional(readOnly = true)
    public List<PostReqDto> findPostsByUser(Account account) {
        List<Post> posts = postRepository.findByAccount(account);
        return posts.stream()
                .map(PostReqDto::fromEntity)
                .collect(Collectors.toList());
    }
}
