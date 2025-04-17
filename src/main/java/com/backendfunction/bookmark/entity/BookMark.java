package com.backendfunction.bookmark.entity;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.entity.BaseEntity;
import com.backendfunction.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id" , nullable = true)
    private Post post;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = true)
    private Account account;

}
