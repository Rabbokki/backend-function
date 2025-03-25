package com.backendfunction.Liquor.service;

import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.Liquor.repository.LiquorRepository;
import com.backendfunction.post.controller.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LiquorService {
    private final LiquorRepository liquorRepository;
    private final ImageRepository imageRepository;


    public LiquorService(LiquorRepository liquorRepository, ImageRepository imageRepository) {
        this.liquorRepository = liquorRepository;
        this.imageRepository = imageRepository;
    }

    public List<LiquorDto> findAll() {
        List<Liquor> liquors = liquorRepository.findAll();

//        Image image = imageRepository.findByPostId();
        return liquors.stream().map(x -> LiquorDto.fromEntity(x)).toList();
    }

    public Map<String, Object> findByLiquorId(Long id) {
        Liquor liquor = liquorRepository.findById(id).orElse(null);
        Map<String, Object> data = new HashMap<>();
        if (ObjectUtils.isEmpty(liquor)) {
            data.put("dto", null);
        } else {
            data.put("dto", LiquorDto.fromEntity(liquor));
        }
        return data;
    }

    public void deleteByLiquorId(Long id) {
        liquorRepository.deleteById(id);
    }

    public void createLiquor(LiquorDto dto) {
        liquorRepository.save(LiquorDto.fromDto(dto));
    }
//    public List<LiquorDto> findByCategory(Category enumCategory) {
//        List<Liquor> liquors = liquorRepository.findByCategory(enumCategory);
//        return liquors.stream().map(x -> LiquorDto.fromEntity(x)).toList();
//    }

//    public List<LiquorDto> findByCategory(Category ) {
//        List<Liquor> liquors = liquorRepository.findByCategory(category1);
//        return liquors.stream().map(x -> LiquorDto.fromEntity(x)).toList();
//    }
}
