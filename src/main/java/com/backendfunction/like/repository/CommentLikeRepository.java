package com.backendfunction.like.repository;

import com.backendfunction.account.entity.Account;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.like.entity.CommentLike;
import com.backendfunction.like.entity.PostLike;
import com.backendfunction.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    boolean existsByCommentIdAndAccount(Long commentId, Account account);
    void deleteByCommentIdAndAccount(Long commentId, Account account);
    Optional<CommentLike> findByAccountAndComment(Account account, Comment comment);
}
