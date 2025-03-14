package com.backendfunction.Liquor.service;

import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.Liquor.repository.LiquorRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiquorService {
    private final LiquorRepository liquorRepository;

    public LiquorService(LiquorRepository liquorRepository) {
        this.liquorRepository = liquorRepository;
    }

    public List<LiquorDto> findAll() {
        List<Liquor> liquors = liquorRepository.findAll();
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

    public void createLiquor(LiquorDto dto) {
        Liquor liquor = LiquorDto.fromDto(dto);
        liquorRepository.save(liquor);

    }

    public void updateLiquor(LiquorDto dto) {
        Liquor liquor = LiquorDto.fromDto(dto);
        liquorRepository.save(liquor);
    }

    public void deleteByLiquorId(Long id) {
        liquorRepository.deleteById(id);

    }

}
