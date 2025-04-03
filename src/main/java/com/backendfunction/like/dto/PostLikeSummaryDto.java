package com.backendfunction.like.dto;

import com.backendfunction.bookmark.entity.BookMark;
import com.backendfunction.like.entity.PostLike;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostLikeSummaryDto {
    private Long id;
    private Long postId;
    private int postPrice;
    private String postTitle;
    private int likeSize;
    private String sellerNickname;
    private LocalDateTime createdAt;

    private String imgUrl;
    private String profilePic;

    public PostLikeSummaryDto(PostLike postLike) {
        this.id = postLike.getId();
        this.postId = postLike.getPost().getId();
        this.postPrice = postLike.getPost().getPrice();
        this.postTitle = postLike.getPost().getTitle();
        this.likeSize = postLike.getPost().getLikeSize();
        this.sellerNickname = postLike.getPost().getAccount().getNickname();
        this.createdAt = postLike.getPost().getCreatedAt();
        this.imgUrl = postLike.getPost().getImages().isEmpty() ? null : postLike.getPost().getImages().get(0).getImage();
        this.profilePic = (postLike.getPost().getAccount().getImgUrl() == null || postLike.getAccount().getImgUrl().isEmpty())
                ? null
                : postLike.getAccount().getImgUrl();
    }
}

