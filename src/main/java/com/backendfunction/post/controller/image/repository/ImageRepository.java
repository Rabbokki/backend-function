package com.backendfunction.post.controller.image.repository;

import com.backendfunction.post.controller.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
