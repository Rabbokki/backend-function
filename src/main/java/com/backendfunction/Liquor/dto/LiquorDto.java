package com.backendfunction.Liquor.dto;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.Liquor.entity.Liquor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiquorDto {
    private Long id;
    private String name;
    private int price;
    private int stock;
    private String country;
    private int count;
    private int totalPrice;
    private List<CartDto> cartDtos = new ArrayList<>();


    public static LiquorDto fromEntity(Liquor liquor) {
        int totalPrice = liquor.getPrice() * liquor.getCount();
        return new LiquorDto(
                liquor.getId(),
                liquor.getName(),
                liquor.getPrice(),
                liquor.getStock(),
                liquor.getCountry(),
                liquor.getCount(),
                totalPrice,
                liquor.getCarts().stream().map(x -> CartDto.fromEntity(x)).toList()
        );
    }

    public static Liquor fromDto(LiquorDto dto) {
        Liquor liquor = new Liquor();
        liquor.setId(dto.getId());
        liquor.setName(dto.getName());
        liquor.setPrice(dto.getPrice());
        liquor.setStock(dto.getStock());
        liquor.setCountry(dto.getCountry());
        liquor.setCount(dto.getCount());
        liquor.setTotalPrice(dto.getTotalPrice());
        return liquor;
    }
}
//private Long id;
//    @Column(length = 100)
//    private String name;
//    private int price;
//    private int stock;
//    @Column(length = 50)
//    private String county;
//    @Enumerated(EnumType.STRING)
//    private Category category;