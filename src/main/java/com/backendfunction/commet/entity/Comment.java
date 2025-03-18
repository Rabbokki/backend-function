package com.backendfunction.commet.entity;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.entity.BaseEntity;
import com.backendfunction.post.entity.Post;
import com.backendfunction.recomment.entity.Recomment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_id")
    private Account account;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Recomment> recomments = new ArrayList<>();
    private int likeSize;

    public Comment(String content, Post post, Account account) {
        this.content = content;
        this.post = post;
        this.account = account;
    }
    public void updateLikeSize(int size){
        this.likeSize = size;
    }
}
