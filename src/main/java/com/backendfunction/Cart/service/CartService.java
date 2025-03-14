package com.backendfunction.Cart.service;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Cart.repository.CartRepository;
import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.Liquor.repository.LiquorRepository;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
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
    private final LiquorRepository liquorRepository;
    private final AccountRepository accountRepository;

    public CartService(CartRepository cartRepository, LiquorRepository liquorRepository, AccountRepository accountRepository) {
        this.cartRepository = cartRepository;
        this.liquorRepository = liquorRepository;
        this.accountRepository = accountRepository;
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



    public void updateByCart(CartDto dto) {
        Cart cart = CartDto.fromDto(dto);
        cartRepository.save(cart);
    }

    public void deleteByCartId(Long id) {
        cartRepository.deleteById(id);

    }


    public void insertCart(CartDto dto, Long id) {
        Liquor liquor = liquorRepository.findById(id).orElse(null);
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);

        Cart cart = new Cart();
        cart.setCount(dto.getCount());
        cart.setPrice(dto.getPrice());
        cart.setLiquor(liquor);
        cart.setAccount(account);

        cartRepository.save(cart);
    }
}
