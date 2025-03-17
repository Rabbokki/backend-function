package com.backendfunction.like.repository;

import com.backendfunction.like.entity.PostLike;
import com.backendfunction.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
}
