package com.backendfunction.cart.dto;

import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.cart.entity.Cart;
import com.backendfunction.post.dto.PostReqDto;
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
    private PostReqDto postReqDto;

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
    if (cart.getPost() != null) {
        dto.setPostReqDto(new PostReqDto(cart.getPost()));

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
