package com.backendfunction.Liquor.controller;

import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.service.LiquorService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LiquorController {
    private final LiquorService liquorService;


    public LiquorController(LiquorService liquorService) {
        this.liquorService = liquorService;
    }

    @GetMapping("/liquor")
    public ResponseEntity<?> findAll(){
        List<LiquorDto> dtos = liquorService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/liquor/id/{id}")
    public ResponseEntity<?> findByLiquor(@PathVariable("id") Long id) throws BadRequestException {
        LiquorDto findLiquor = getDto(id, "조회실패");
        return ResponseEntity.status(HttpStatus.OK).body(findLiquor);
    }
//    카테고리별 출력
<<<<<<< HEAD
@GetMapping("/liquor/category/{category}")
public ResponseEntity<?> findByCategory(@PathVariable("category")String category) {
    Category enumCategory = Category.valueOf(category.toUpperCase());
    List<LiquorDto> list = liquorService.findByCategory(enumCategory);
    return ResponseEntity.status(HttpStatus.OK).body(list);
}
=======
//@GetMapping("/liquor/category/{category}")
//public ResponseEntity<?> findByCategory(@PathVariable("category")String category) {
//    List<LiquorDto> list = liquorService.findByCategory(category);
//    return ResponseEntity.status(HttpStatus.OK).body(list);
//}
>>>>>>> feature-joo-fix-img

    @PostMapping("/liquor/create")
    public ResponseEntity<?> createLiquor(@RequestBody LiquorDto dto) {
        liquorService.createLiquor(dto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @PatchMapping("/liquor/update/{id}")
    public ResponseEntity<?> updateLiquor(@RequestBody LiquorDto dto, @PathVariable("id") Long id) {
        if (!dto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("실패");
        }
        liquorService.updateLiquor(dto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @DeleteMapping("/liquor/delete/{id}")
    public ResponseEntity<?> deleteByLiquorId(@PathVariable("id") Long id) throws BadRequestException {
        LiquorDto dto = getDto(id, "Liquor 삭제 실패");
        liquorService.deleteByLiquorId(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    private LiquorDto getDto(Long id, String message) throws BadRequestException {
        Map<String, Object> findLiquor = liquorService.findByLiquorId(id);
        if (ObjectUtils.isEmpty(findLiquor.get("dto"))) {
            throw new BadRequestException(message);
        }
        return (LiquorDto) findLiquor.get("dto");
    }
}
