package com.backendfunction.post.dto;

import com.backendfunction.account.entity.Account;
import com.backendfunction.commet.dto.CommentReqDto;
import com.backendfunction.global.image.entity.Image;
import com.backendfunction.post.entity.Post;
import com.backendfunction.recomment.dto.RecommentReqDto;
import com.backendfunction.recomment.dto.RecommentResDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDto {
    private String title;
    private String content;
    private int price;
    private MultipartFile img;
    private List<String> imageUrls;
    private List<CommentReqDto> comments;

    public PostReqDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
    }
    public PostReqDto(String title, String content, int price, List<String> imageUrls,
                      List<CommentReqDto> comments) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.imageUrls = imageUrls;
        this.comments = comments;
    }

    public static PostReqDto fromEntity(Post post){
        return new PostReqDto(
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getImages().stream().map(Image::getImage)
                        .collect(Collectors.toList()),
                post.getCommentList().stream()
                        .map(comment -> new CommentReqDto(
                                comment.getId(),
                                comment.getContent(),
                                comment.getRecomments().stream().map(RecommentResDto::fromEntity)
                                        .collect(Collectors.toList())
                        )).collect(Collectors.toList())
        );
    }

    public static Post fromDto(PostReqDto dto, Account account){
        Post post = new Post(dto,account);
        return post;
    }
}
