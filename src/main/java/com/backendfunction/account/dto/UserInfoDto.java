package com.backendfunction.account.dto;

import com.backendfunction.account.entity.Account;
import com.backendfunction.like.dto.PostLikeDto;
import com.backendfunction.post.dto.PostSummaryDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.review.dto.ReviewSummaryDto;
import com.backendfunction.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class UserInfoDto {
    private Long accountId;
    private String email; // 추가
    private String nickname;
    private LocalDate birthday;
    private String imgUrl;
    private List<PostSummaryDto> postList = new ArrayList<>();
    private List<ReviewSummaryDto> reviews = new ArrayList<>();
    private List<PostLikeDto> postLikes = new ArrayList<>();

    @Builder
    public UserInfoDto(Account account) {
        this.accountId = account.getId();
        this.email = account.getEmail();
        this.nickname = account.getNickname();
        this.birthday = account.getBirthday();
        this.imgUrl = account.getImgUrl();
        this.postList = account.getPosts().stream().map(PostSummaryDto::new).collect(Collectors.toList());
        this.reviews = account.getReviews().stream().map(ReviewSummaryDto::new).collect(Collectors.toList());
        this.postLikes = account.getPostLikes().stream().map(PostLikeDto::new).collect(Collectors.toList());
    }
}