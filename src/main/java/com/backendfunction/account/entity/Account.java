package com.backendfunction.account.entity;

import com.backendfunction.bookmark.entity.BookMark;
import com.backendfunction.cart.entity.Cart;
import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.like.entity.CommentLike;
import com.backendfunction.like.entity.PostLike;
import com.backendfunction.post.entity.Post;
import com.backendfunction.recomment.entity.Recomment;
import com.backendfunction.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    private String email;
    private String password;
    private String nickname;

    private LocalDate birthday;

    private String imgUrl;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(fetch =  FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Recomment> recomments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<BookMark> bookMarks = new ArrayList<>();




    public Account(AccountReqDto accountReqDto) {
        this.email = accountReqDto.getEmail();
        this.password = accountReqDto.getPassword();
        this.nickname = accountReqDto.getNickname();
        this.birthday = accountReqDto.getBirthday();
        this.imgUrl = accountReqDto.getImgUrl();
    }
}
