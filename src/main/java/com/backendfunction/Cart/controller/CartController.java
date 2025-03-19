package com.backendfunction.Cart.controller;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.Liquor.service.LiquorService;
import com.backendfunction.Cart.service.CartService;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.service.AccountService;
import com.backendfunction.global.security.user.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final AccountService accountService;
    private final LiquorService liquorService;

    public CartController(CartService cartService, AccountService accountService, LiquorService liquorService) {
        this.cartService = cartService;
        this.accountService = accountService;
        this.liquorService = liquorService;
    }

    @PostMapping(value = "/add/{liquorId}")
    public ResponseEntity<?> addCart(@PathVariable("liquorId") Long id,
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
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteByCartId(id, userDetails.getAccount());
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }


}
