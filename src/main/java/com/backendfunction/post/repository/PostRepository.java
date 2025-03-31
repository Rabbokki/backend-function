package com.backendfunction.post.repository;

import com.backendfunction.account.entity.Account;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.enums.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
//    @Modifying
//    @Query(value = "SELECT * FROM post WHERE category = :category ", nativeQuery = true)
//    Optional<Post> findById(Long id);
    List<Post> findByAccount(Account account);
    Post findPostByIdAndAccount(Long id, Account account);

    List<Post> findByCategory(Category category);
}
