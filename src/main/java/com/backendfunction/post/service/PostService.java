package com.backendfunction.post.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.image.entity.Image;
import com.backendfunction.global.image.repository.ImageRepository;
import com.backendfunction.post.dto.PostDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import com.backendfunction.s3.S3Service;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    public ResponseDto<?> createPost(PostDto dto, List<MultipartFile> file, Account account) {
        List<Image> imageList = new ArrayList<>();
        Post post = new Post(dto,account);

        for (MultipartFile multipartFile : file) {
            Image image = imageRepository.save(new Image(s3Service.uploadFile(multipartFile), post));
            imageList.add(image);
        }
        postRepository.save(post);
        PostDto postDto = new PostDto(post);

        List<String> images = new ArrayList<>();
        for(Image image : imageList){
            images.add(image.getImage());
        }
        postDto.setImgs(images);
        return ResponseDto.success(postDto);
    }

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

    public void updateByPost(PostDto dto) {
        Post post = PostDto.fromDto(dto);
        postRepository.save(post);
    }

    public void deleteByPostId(Long id) {
        postRepository.deleteById(id);

    }

    public List<PostDto> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(x -> PostDto.fromEntity(x)).toList();
    }

    public PostDto findByid(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        return PostDto.fromEntity(post);
    }
//    public List<PostDto> findByCategory(Category category) {
//        List<Post> posts = postRepository.findByCategory(category);
//        return posts.stream().map(x -> PostDto.fromEntity(x)).toList();
//    }


}
