package com.backendfunction.like.repository;

import com.backendfunction.like.entity.CommentLike;
import com.backendfunction.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
}
