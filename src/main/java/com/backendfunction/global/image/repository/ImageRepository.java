package com.backendfunction.global.image.repository;

import com.backendfunction.global.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
