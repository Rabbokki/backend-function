package com.backendfunction.post.entity;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.entity.BaseEntity;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.global.image.entity.Image;
import com.backendfunction.post.dto.PostReqDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> commentList = new ArrayList<>();

    // image랑 매핑함 from JJJ
    @JsonManagedReference
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "a_id", nullable = true)
    private Account account;

    public Post(PostReqDto dto, Account account) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = dto.getPrice();
        this.account = account;
    }
}
