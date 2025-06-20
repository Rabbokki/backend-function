package com.backendfunction.cart.controller;

import com.backendfunction.cart.dto.CartDto;
import com.backendfunction.Liquor.service.LiquorService;
import com.backendfunction.cart.service.CartService;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.service.AccountService;
import com.backendfunction.global.security.user.UserDetailsImpl;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final AccountService accountService;
    private final LiquorService liquorService;

    public CartController(CartService cartService, AccountService accountService, LiquorService liquorService) {
        this.cartService = cartService;
        this.accountService = accountService;
        this.liquorService = liquorService;
    }

    @PostMapping(value = "/add/{postId}")
    public ResponseEntity<?> addCart(@PathVariable("postId") Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails, CartDto cartDto
    ) {

        if (userDetails == null || userDetails.getAccount() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }
        Account account = userDetails.getAccount();
        cartService.addCart(id, account ,cartDto);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteByCartId(id, userDetails.getAccount());
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @GetMapping("/find")
    public ResponseEntity<?> findByAccountId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CartDto> cartDtos = cartService.findByAccountIdWithPost(userDetails.getAccount().getId());
        return ResponseEntity.status(HttpStatus.OK).body(cartDtos);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<?> updateById(@PathVariable("postId") Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestParam("status") Integer statue) {
        cartService.updateById(postId, userDetails.getAccount(), statue);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }



}