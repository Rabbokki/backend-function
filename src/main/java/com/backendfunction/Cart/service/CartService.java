package com.backendfunction.Cart.service;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Cart.repository.CartRepository;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.Liquor.repository.LiquorRepository;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.global.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
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


    public ResponseDto<?> addCart(Long id, Account account , CartDto cartDto) {
        Liquor liquor = liquorRepository.findById(id).orElse(null);
        if (ObjectUtils.isEmpty(liquor)) {
            return ResponseDto.fail("100", "찾을수 없는 술입니다");
        }

        Cart cart = cartRepository.findByLiquorAndAccount(liquor, account);
        if (cart != null) {
            cart.setCount(cart.getCount() + 1);
            cart.setPrice(cart.getPrice() + liquor.getPrice());
            cartRepository.save(cart);
            CartDto dto = new CartDto(cart);
            return ResponseDto.success(dto);
        } else {
            int price = liquor.getPrice() * 1;
            Cart cart1 = new Cart(liquor, account, 1, price);
            cartRepository.save(cart1);
            CartDto dto = new CartDto(cart1);
            return ResponseDto.success(dto);
        }

    }

    public ResponseDto<?> deleteByCartId(Long id, Account account) {
        Cart cart = cartRepository.findByidAndAccount(id, account);
        if (cart == null) return ResponseDto.fail("100", "삭제할수없습니다");
        cartRepository.delete(cart);
        return ResponseDto.success("삭제 성공");
    }

}
