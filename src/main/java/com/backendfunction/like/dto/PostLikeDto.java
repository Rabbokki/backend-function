package com.backendfunction.like.dto;

import com.backendfunction.like.entity.PostLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLikeDto {

    private Long id;
    private Long postId;
    private Long accountId;

    public PostLikeDto(PostLike postLike) {
        this.id = postLike.getId();
        this.postId = postLike.getPost().getId();
        this.accountId = postLike.getAccount().getId();
    }
}

