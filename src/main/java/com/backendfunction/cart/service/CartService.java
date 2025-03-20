package com.backendfunction.cart.service;

import com.backendfunction.cart.dto.CartDto;
import com.backendfunction.cart.entity.Cart;
import com.backendfunction.cart.repository.CartRepository;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.Liquor.repository.LiquorRepository;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.global.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

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




    public List<CartDto> findByAccountIdWithLiquor(Long id) {
        List<Cart> carts = cartRepository.findByAccountIdWithLiquor(id);
//        return carts.stream().map(cart -> CartDto.fromEntity(cart)).collect(Collectors.toList());
        return carts.stream().map(x -> CartDto.fromEntity(x)).toList();
    }

    public ResponseDto<?> updateById(Long liquorId, Account account, int status) {
        Cart cart =  cartRepository.findByLiquorIdAndAccount(liquorId, account);
        if (cart == null) return ResponseDto.fail("100", "장바구니에서 찾을수 없습니다");

        if (status > 0) {
            cart.setCount(cart.getCount() + status );
        }
        if (status < 0) {
            int newCount = cart.getCount() + status;
            if (newCount < 0) {
                newCount = 0;
            }
            cart.setCount(newCount);

        }
        cartRepository.save(cart);
        return ResponseDto.success("장바구니가 성공적으로 업데이트 되었습니다");
    }

}
