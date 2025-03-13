package com.backendfunction.Liquor.repository;

import com.backendfunction.Liquor.constant.Category;
import com.backendfunction.Liquor.entity.Liquor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface LiquorRepository extends JpaRepository<Liquor,Long> {
    List<Liquor> findByCategory(String category);
}
