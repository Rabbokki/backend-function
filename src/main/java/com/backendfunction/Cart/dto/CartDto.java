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
    private List<LiquorDto> list = new ArrayList<>();
    private Long accountId;

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.count = cart.getCount();
        this.price = cart.getPrice();
        this.list = cart.getLiquors().stream().map(x -> LiquorDto.fromEntity(x)).toList();

    }

    public static CartDto fromEntity(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getCount(),
                cart.getPrice(),
                cart.getLiquors().stream().map(x->LiquorDto.fromEntity(x)).toList(),
                cart.getAccount().getId()
        );
    }

    public static Cart fromDto(CartDto dto) {
        Account account = new Account();
        account.setId(dto.getAccountId());
        Cart cart = new Cart(dto.getId(), dto.getCount(), dto.getPrice(), account);
        return cart;
    }

}
