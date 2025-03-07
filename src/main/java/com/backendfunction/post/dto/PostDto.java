package com.backendfunction.post.dto;

import com.backendfunction.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String content;

    public static PostDto fromEntity(Post post) {
        return new PostDto(
                post.getId(),
                post.getContent()
        );
    }

    public static Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setId(dto.getId());
        post.setContent(dto.getContent());
        return post;
    }
}
