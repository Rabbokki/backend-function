package com.backendfunction.cart.dto;

import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private int count;
    private int price;
    private LiquorDto liquorDto;

    public CartDto(Cart cart) {
    }

//    public static CartDto fromEntity(Cart cart) {
//        return new CartDto(
//                cart.getId(),
//                cart.getCount(),
//                cart.getPrice()
//        );
//    }
public static CartDto fromEntity(Cart cart) {
    CartDto dto = new CartDto();
    dto.setId(cart.getId());
    dto.setCount(cart.getCount());
    dto.setPrice(cart.getPrice());
    if (cart.getLiquor() != null) {
        dto.setLiquorDto(new LiquorDto(cart.getLiquor()));

    }
    return dto;
}

    public static Cart fromDto(CartDto dto) {
        Cart cart = new Cart();
        cart.setId(dto.getId());
        cart.setCount(dto.getCount());
        cart.setPrice(dto.getPrice());
        return cart;
    }

}
