package com.backendfunction.post.repository;

import com.backendfunction.post.constant.Category;
import com.backendfunction.post.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
//    @Modifying
//    @Query(value = "SELECT * FROM post WHERE category = :category ", nativeQuery = true)
    List<Post> findByCategory(Category category);

}
