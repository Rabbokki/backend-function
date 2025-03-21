package com.backendfunction.cart.service;

import com.backendfunction.cart.dto.CartDto;
import com.backendfunction.cart.entity.Cart;
import com.backendfunction.cart.repository.CartRepository;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.Liquor.repository.LiquorRepository;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
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
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public CartService(CartRepository cartRepository, LiquorRepository liquorRepository, AccountRepository accountRepository, PostRepository postRepository) {
        this.cartRepository = cartRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
    }


    public ResponseDto<?> addCart(Long id, Account account , CartDto cartDto) {
        Post post = postRepository.findById(id).orElse(null);
        if (ObjectUtils.isEmpty(post)) {
            return ResponseDto.fail("100", "찾을수 없는 술입니다");
        }
        Cart cart = cartRepository.findByPostAndAccount(post, account);
        if (cart != null) {
            cart.setCount(cart.getCount() +1 );
            cart.setPrice(cart.getPrice() + post.getPrice());
            cartRepository.save(cart);
            CartDto dto = new CartDto(cart);
            return ResponseDto.success(dto);
        }else {
            int price = post.getPrice() * 1;
            Cart cart1 = new Cart(post, account, 1, price);
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




    public List<CartDto> findByAccountIdWithPost(Long id) {
        List<Cart> carts = cartRepository.findByAccountIdWithPost(id);
//        return carts.stream().map(cart -> CartDto.fromEntity(cart)).collect(Collectors.toList());
        return carts.stream().map(x -> CartDto.fromEntity(x)).toList();
    }

    public ResponseDto<?> updateById(Long postId, Account account, int status) {
        Cart cart =  cartRepository.findByPostIdAndAccount(postId, account);
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
