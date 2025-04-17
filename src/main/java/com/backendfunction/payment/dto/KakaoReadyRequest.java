package com.backendfunction.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoReadyRequest {  // New DTO to accept frontend data
    private Long postId;
    private int amount;
    private int quantity;
    private String userEmail;
    private String itemName;
    private int totalAmount;
    private int vatAmount;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;
}
