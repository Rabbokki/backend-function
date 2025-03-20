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
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDto {
    private String title;
    private String content;
    private int price;
    private MultipartFile img;
    private List<String> imageUrls = new ArrayList<>();
    private List<CommentReqDto> comments;
    private int likeCount;
    private double averageRating;
    private int reviewSize;

    public PostReqDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.likeCount = post.getLikeSize();
        this.imageUrls = post.getImages().stream().map(Image::getImage).collect(Collectors.toList());
        this.averageRating = post.getAverageRating();
        this.reviewSize = post.getReviewSize();
    }

    public PostReqDto(String title, String content, int price, List<String> imageUrls,
                      List<CommentReqDto> comments, int likeCount, double averageRating, int reviewSize) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.imageUrls = imageUrls;
        this.comments = comments;
        this.likeCount = likeCount;
        this.averageRating = averageRating;
        this.reviewSize = reviewSize;
    }

    public static PostReqDto fromEntity(Post post) {
        return new PostReqDto(
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getImages().stream().map(Image::getImage).collect(Collectors.toList()),
                post.getCommentList().stream()
                        .map(comment -> new CommentReqDto(
                                comment.getId(),
                                comment.getContent(),
                                comment.getLikeSize(),
                                comment.getRecomments().stream().map(RecommentResDto::fromEntity)
                                        .collect(Collectors.toList())
                        )).collect(Collectors.toList()),
                post.getLikeSize(),
                post.getAverageRating(),
                post.getReviewSize()
        );
    }

    public static Post fromDto(PostReqDto dto, Account account) {
        return new Post(dto, account);
    }
}