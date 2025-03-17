package com.backendfunction.recomment.repository;

import com.backendfunction.recomment.entity.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommentRepository extends JpaRepository<Recomment, Long> {
    List<Recomment> findRecommentByCommentId(Long commentId);
}
