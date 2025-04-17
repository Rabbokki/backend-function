package com.backendfunction.cart.dto;

import lombok.Data;

@Data
public class CartInput {
    private Long id;
    private Long liquorId;
    private int count;
    private int price;
}
