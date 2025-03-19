package com.backendfunction.Cart.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartInput {
    private Long id;
    private Long liquorId;
    private int count;
    private int price;
}
