package com.backendfunction.post.dto;

import com.backendfunction.account.entity.Account;
import com.backendfunction.commet.dto.CommentReqDto;
import com.backendfunction.global.image.entity.Image;
import com.backendfunction.global.util.Chrono;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.enums.Category;
import com.backendfunction.recomment.dto.RecommentResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDto {
    private Long id;
    private String title;
    private Category category;
    private String content;
    private int price;
    private int stock;
    private List<String> imageUrls = new ArrayList<>();
    private List<CommentReqDto> comments;
    private Long viewCount;
    private int likeCount;
    private double averageRating;
    private int reviewSize;
    private String sellerEmail;
    private String sellerNickname;
    private String timeAgo;
//    public Category getCategory() {
//        return category != null ? Category.valueOf(category) : null; // 문자열을 Category로 변환
//    }
    public PostReqDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.stock = post.getStock();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeSize();
        this.imageUrls = post.getImages().stream().map(Image::getImage).collect(Collectors.toList());
        this.averageRating = post.getAverageRating();
        this.reviewSize = post.getReviewSize();
        this.sellerEmail = post.getAccount().getEmail();
        this.sellerNickname = post.getAccount().getNickname();
        this.timeAgo = Chrono.timesAgo(post.getCreatedAt());
    }



    public PostReqDto(Long id, String title, String content, int price, int stock, List<String> imageUrls,
                      List<CommentReqDto> comments,Long viewCount, int likeCount, double averageRating, int reviewSize, String sellerEmail, String sellerNickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.stock = stock;
        this.imageUrls = imageUrls;
        this.comments = comments;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.averageRating = averageRating;
        this.reviewSize = reviewSize;
        this.sellerEmail = sellerEmail;
        this.sellerNickname = sellerNickname;
    }



    public static PostReqDto fromEntity(Post post) {
        return new PostReqDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getStock(),
                post.getImages().stream().map(Image::getImage).collect(Collectors.toList()),
                post.getCommentList().stream()
                        .map(comment -> new CommentReqDto(
                                comment.getId(),
                                comment.getContent(),
                                comment.getLikeSize(),
                                comment.getRecomments().stream().map(RecommentResDto::fromEntity)
                                        .collect(Collectors.toList())
                        )).collect(Collectors.toList()),
                post.getViewCount(),
                post.getLikeSize(),
                post.getAverageRating(),
                post.getReviewSize(),
                post.getAccount().getEmail(),
                post.getAccount().getNickname()
        );
    }

    public static Post fromDto(PostReqDto dto, Account account) {
        return new Post(dto, account);
    }
}