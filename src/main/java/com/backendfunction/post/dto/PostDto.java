package com.backendfunction.post.dto;

import com.backendfunction.commet.dto.CommentDto;
import com.backendfunction.post.constant.Category;
import com.backendfunction.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private String title;
    private String price;
    private Category category;
    private List<CommentDto> commentDtos = new ArrayList<>();

    public static PostDto fromEntity(Post post) {
        return new PostDto(
                post.getId(),
                post.getContent(),
                post.getTitle(),
                post.getPrice(),
                post.getCategory(),
                post.getCommentList().stream().map(x -> CommentDto.fromEntity(x)).toList()
        );
    }

    public static Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setId(dto.getId());
        post.setContent(dto.getContent());
        post.setTitle(dto.getTitle());
        post.setPrice(dto.getPrice());
        post.setCategory(dto.getCategory());
        return post;
    }

}
