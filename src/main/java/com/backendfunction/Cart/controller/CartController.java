package com.backendfunction.Cart.controller;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.Cart.service.CartService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public ResponseEntity<?> findAllCart() {
        List<CartDto> dtos = cartService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<?> findByCartId(@PathVariable("id") Long id) throws BadRequestException {
        CartDto findByCartId = getDto(id, "조회실패");
        return ResponseEntity.status(HttpStatus.OK).body(findByCartId);
    }

    @PostMapping("/cart/insert")
    public ResponseEntity<?> insertCart(@RequestBody CartDto dto) {
        cartService.insertCart(dto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @PatchMapping("/cart/update/{id}")
    public ResponseEntity<?> updateCartId(@RequestBody CartDto dto, @PathVariable("id") Long id) {
        if (!dto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("실패");
        }
        cartService.updateByCart(dto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @DeleteMapping("/cart/delete/{id}")
    public ResponseEntity<?> deleteByCartId(@PathVariable("id") Long id) throws BadRequestException {
        CartDto dto = getDto(id, "실패");
        cartService.deleteByCartId(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }


    private CartDto getDto(Long id, String message) throws BadRequestException {

        Map<String, Object> findByCartId = cartService.findByCartId(id);
        if (ObjectUtils.isEmpty(findByCartId.get("dto"))) {
            throw new BadRequestException(message);
        }
        return (CartDto) findByCartId.get("dto");
    }

}
