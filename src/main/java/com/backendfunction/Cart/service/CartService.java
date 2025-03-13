package com.backendfunction.Cart.service;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Cart.repository.CartRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    @Autowired
    EntityManager em;

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<CartDto> findAll() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(x -> CartDto.fromEntity(x)).toList();
    }

    public Map<String, Object> findByCartId(Long id) {
        Cart cart = cartRepository.findById(id).orElse(null);
        Map<String, Object> data = new HashMap<>();
        if (ObjectUtils.isEmpty(cart)) {
            data.put("dto", null);
        } else {
            data.put("dto", CartDto.fromEntity(cart));
        }
        return data;
    }

    public void insertCart(CartDto dto) {
        Cart cart = CartDto.fromDto(dto);

        cartRepository.save(cart);
    }

    public void updateByCart(CartDto dto) {
        Cart cart = CartDto.fromDto(dto);
        cartRepository.save(cart);
    }

    public void deleteByCartId(Long id) {
        cartRepository.deleteById(id);

    }
}
