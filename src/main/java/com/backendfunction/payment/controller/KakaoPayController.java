package com.backendfunction.payment.controller;

import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.payment.dto.*;
import com.backendfunction.payment.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    /**
     * 결제요청
     */
    @PostMapping("/ready")
    public ResponseEntity<ResponseDto<KakaoReadyResponse>> readyToKakaoPay(@RequestBody KakaoReadyRequest request) {
        KakaoReadyResponse response = kakaoPayService.kakaoPayReady(request);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    /**
     * 결제 성공
     */
    @GetMapping("/success")
    public ResponseEntity<ResponseDto<KakaoApproveResponse>> afterPayRequest(@RequestParam("pg_token") String pgToken) {
        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);
        return ResponseEntity.ok(ResponseDto.success(kakaoApprove));
    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public ResponseEntity<ResponseDto<?>> cancel() {
        return ResponseEntity.badRequest().body(
                ResponseDto.fail("PAY_CANCEL", "Payment cancellation requested")
        );
    }

    /**
     * 결제 실패
     */
    @GetMapping("/fail")
    public ResponseEntity<ResponseDto<?>> fail() {
        return ResponseEntity.badRequest().body(
                ResponseDto.fail("PAY_FAILED", "Payment failed")
        );
    }

    /**
     * 환불
     */
    @PostMapping("/refund")
    public ResponseEntity<ResponseDto<?>> refund(@RequestBody KakaoCancelRequest request) {
        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel(
                request.getTid(),
                request.getCancelAmount(),
                request.getCancelTaxFreeAmount(),
                request.getCancelVatAmount()
        );
        return ResponseEntity.ok(ResponseDto.success(kakaoCancelResponse));
    }
}