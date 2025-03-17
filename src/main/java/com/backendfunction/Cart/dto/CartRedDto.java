package com.backendfunction.Cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartRedDto {
    private int count;
    private int price;
    private Long accountId;
}
