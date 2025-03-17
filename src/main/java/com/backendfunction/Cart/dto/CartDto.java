package com.backendfunction.Cart.dto;

import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private int count;
    private int price;
    private LiquorDto liquorDto;
    private Long accountId;

    public static CartDto fromEntity(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getCount(),
                cart.getPrice(),
                LiquorDto.fromEntity(cart.getLiquor()),
                cart.getAccount().getId()
        );
    }

    public static Cart fromDto(CartDto dto) {
        Cart cart = new Cart();
        cart.setId(dto.getId());
        cart.setCount(dto.getCount());
        cart.setPrice(dto.getPrice());
        Liquor liquor = LiquorDto.fromDto(dto.getLiquorDto());
        cart.setLiquor(liquor);
        Account account = new Account();
        account.setId(dto.getAccountId());
        cart.setAccount(account);
        return cart;
    }
}
