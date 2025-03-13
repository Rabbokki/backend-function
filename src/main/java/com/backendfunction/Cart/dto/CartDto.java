package com.backendfunction.Cart.dto;

import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
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

    public static CartDto fromEntity(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getCount(),
                cart.getPrice(),
                LiquorDto.fromEntity(cart.getLiquor())
        );
    }

    public static Cart fromDto(CartDto dto) {
        Cart cart = new Cart();
        cart.setId(dto.getId());
        cart.setCount(dto.getCount());
        cart.setPrice(dto.getPrice());
        Liquor liquor = LiquorDto.fromDto(dto.getLiquorDto());
        cart.setLiquor(liquor);
        return cart;
    }
}
