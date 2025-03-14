package com.backendfunction.Liquor.dto;

import com.backendfunction.Liquor.entity.Liquor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiquorDto {
    private Long id;
    private String name;
    private int price;
    private int stock;
    private String country;


    public static LiquorDto fromEntity(Liquor liquor) {
        return new LiquorDto(
                liquor.getId(),
                liquor.getName(),
                liquor.getPrice(),
                liquor.getStock(),
                liquor.getCountry()
        );
    }

    public static Liquor fromDto(LiquorDto dto) {
        Liquor liquor = new Liquor();
        liquor.setId(dto.getId());
        liquor.setName(dto.getName());
        liquor.setPrice(dto.getPrice());
        liquor.setStock(dto.getStock());
        liquor.setCountry(dto.getCountry());
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