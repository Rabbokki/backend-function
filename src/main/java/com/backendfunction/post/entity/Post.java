package com.backendfunction.post.entity;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.entity.BaseEntity;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.global.image.entity.Image;
import com.backendfunction.like.entity.PostLike;
import com.backendfunction.review.entity.Review;
import com.backendfunction.post.dto.PostReqDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String title;
    @Column(length = 500)
    private String content;
    private int price;
    private int commentSize;
    private int likeSize;
    private int reviewSize;
    private double averageRating;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> commentList = new ArrayList<>();

    // image랑 매핑함 from JJJ
    @JsonManagedReference
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "a_id", nullable = true)
    private Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Post(PostReqDto dto, Account account) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = dto.getPrice();
        this.account = account;
    }

    public Post(String title, String content, int price, Account account) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.account = account;
        this.images = new ArrayList<>();  // 이미지 리스트 초기화
        this.commentList = new ArrayList<>();  // 댓글 리스트 초기화
        this.reviewSize = 0;
        this.averageRating = 0;
    }

    public void postLikeUpdate(int size){
        this.likeSize = size;
    }

    public void commentUpdate(int size){
        this.commentSize = size;
    }

    public void reviewUpdate(int size){
        this.reviewSize = size;
    }

    public void recalculateAverageRating() {
        double totalRating = 0;
        int reviewCount = 0;

        for (Review review : reviews) {
            totalRating += review.getRating();
            reviewCount++;
        }

        if (reviewCount > 0) {
            this.averageRating = totalRating / reviewCount;
        } else {
            this.averageRating = 0;
        }
    }

}
