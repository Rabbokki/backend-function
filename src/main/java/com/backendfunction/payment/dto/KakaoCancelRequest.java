package com.backendfunction.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoCancelRequest {
    private String tid;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
    private int cancelVatAmount;
}
