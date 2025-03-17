package com.backendfunction.Cart.service;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.Cart.dto.CartRedDto;
import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Cart.repository.CartRepository;
import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.Liquor.repository.LiquorRepository;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.global.dto.ResponseDto;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CartService {
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

    @Transactional
    public ResponseDto<?> insertCart(CartRedDto dto, Account account) {
        log.info("Insert Cart for account : {}", account != null ? account.getId() : "null");
        Cart cart = new Cart(dto, account);
        cartRepository.save(cart);
        CartDto dto1 = new CartDto(cart);
        return ResponseDto.success(dto1);
    }
    @Transactional
    public ResponseDto<?> deleteByCartId(Long id, Account account) {
        Cart cart = cartRepository.findCartByIdAndAccount(id, account);
        if (cart == null) return ResponseDto.fail("100", "삭제실패");
        cartRepository.delete(cart);
        return ResponseDto.success("삭제 완");

    }


//    public void insertCart(CartDto dto, Long id) {
//        Liquor liquor = liquorRepository.findById(id).orElse(null);
//        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
//
//        Cart cart = new Cart();
//        cart.setCount(dto.getCount());
//        cart.setPrice(dto.getPrice());
//        cart.setLiquor(liquor);
//        cart.setAccount(account);
//
//        cartRepository.save(cart);
//    }
}
