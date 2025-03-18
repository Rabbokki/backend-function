package com.backendfunction.like.repository;

import com.backendfunction.account.entity.Account;
import com.backendfunction.like.entity.PostLike;
import com.backendfunction.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    boolean existsByAccountAndPost(Account account, Post post);
    void deleteByAccountAndPost(Account account, Post post);
    Optional<PostLike> findByAccountAndPost(Account account, Post post);
}
