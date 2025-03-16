package com.backendfunction.post.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.global.dto.GlobalResDto;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.image.entity.Image;
import com.backendfunction.global.image.repository.ImageRepository;
import com.backendfunction.post.dto.PostDto;
import com.backendfunction.post.dto.PostReqDto;
import com.backendfunction.post.dto.PostUpReqDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import com.backendfunction.s3.S3Service;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Post post = new Post(dto, account);

        // Post 먼저 저장
        post = postRepository.save(post);
        log.info("Post saved with ID: {}", post.getId());

        List<Image> imageList = new ArrayList<>();
        if (file != null && !file.isEmpty()) {
            for (MultipartFile multipartFile : file) {
                log.info("Processing file: {}", multipartFile.getOriginalFilename());
                Image image = new Image(s3Service.uploadFile(multipartFile), post);
                post.getImages().add(image);
                imageList.add(image);
            }
        }

        // Post와 Image 함께 저장
        postRepository.save(post);
        PostDto postDto = new PostDto(post); // 완성된 생성자 사용
        return ResponseDto.success(postDto);
    }
//    public ResponseDto<?> createPost(PostDto dto, List<MultipartFile> file, Account account) {
//        List<Image> imageList = new ArrayList<>();
//        Post post = new Post(dto,account);
//
//        for (MultipartFile multipartFile : file) {
//            Image image = imageRepository.save(new Image(s3Service.uploadFile(multipartFile), post));
//            imageList.add(image);
//        }
//        postRepository.save(post);
//        PostDto postDto = new PostDto(post);
//
//        List<String> images = new ArrayList<>();
//        for(Image image : imageList){
//            images.add(image.getImage());
//        }
//        postDto.setImgs(images);
//        return ResponseDto.success(postDto);
//    }

    public Map<String, Object> findByPostId(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        Map<String, Object> data = new HashMap<>();
        if (ObjectUtils.isEmpty(post)) {
            data.put("dto", null);
        } else {
            data.put("dto", PostDto.fromEntity(post));
        }
        return data;
    }

//    public void updateByPost(PostDto dto) {
//        Post post = PostDto.fromDto(dto);
//        postRepository.save(post);
//    }
    public ResponseDto<?> updateByPost(Long id, List<MultipartFile> imgs, PostUpReqDto dto, Account account) {
        Post post = postRepository.findPostByIdAndAccount(id,account);
        if(post == null) return ResponseDto.fail("600","수정 권한이 없습니다.");

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
    public ResponseDto<?> deleteByPostId(Long id,Account account) {
        Post post = postRepository.findPostByIdAndAccount(id,account);
        if(post==null) return ResponseDto.fail("600","삭제 권한이 없습니다.");
        postRepository.delete(post);
        return ResponseDto.success("삭제 완료");

    }

    @Transactional(readOnly = true)
    public List<PostDto> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(x -> PostDto.fromEntity(x)).toList();
    }
    @Transactional(readOnly = true)
    public PostDto findByid(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        return PostDto.fromEntity(post);
    }

//    public List<PostDto> findByCategory(Category category) {
//        List<Post> posts = postRepository.findByCategory(category);
//        return posts.stream().map(x -> PostDto.fromEntity(x)).toList();
//    }


}
