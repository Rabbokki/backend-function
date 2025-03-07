package com.backendfunction.post.service;

import com.backendfunction.post.dto.PostDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void createPost(PostDto dto) {
        Post post = PostDto.fromDto(dto);
        postRepository.save(post);
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

}
