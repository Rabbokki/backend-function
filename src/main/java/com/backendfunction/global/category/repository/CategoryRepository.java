package com.backendfunction.global.category.repository;

import com.backendfunction.global.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
