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

    public CartDto(Cart cart) {
    }

    public static CartDto fromEntity(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getCount(),
                cart.getPrice()
        );
    }

    public static Cart fromDto(CartDto dto) {
        Cart cart = new Cart();
        cart.setId(dto.getId());
        cart.setCount(dto.getCount());
        cart.setPrice(dto.getPrice());
        return cart;
    }

}
