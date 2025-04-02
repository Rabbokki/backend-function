package com.backendfunction.payment.service;

import com.backendfunction.account.dto.UserInfoDto;
import com.backendfunction.account.service.AccountService;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.payment.dto.KakaoApproveResponse;
import com.backendfunction.payment.dto.KakaoCancelResponse;
import com.backendfunction.payment.dto.KakaoReadyRequest;
import com.backendfunction.payment.dto.KakaoReadyResponse;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {
    private final PostRepository postRepository;
    private final AccountService accountService;

    @Value("${kakao.admin-key}")
    private String adminKey;
    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    private KakaoReadyResponse kakaoReady;

    public KakaoReadyResponse kakaoPayReady(KakaoReadyRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get user info using userDetails
        UserInfoDto userInfo = accountService.getUserInfoByEmail(userDetails);
        Long accountId = userInfo.getAccountId();

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", request.getPostId().toString()); // Use postId as order ID
        parameters.add("partner_user_id", accountId.toString()); // Set accountId instead of email
        parameters.add("item_name", request.getItemName()); // Use item name from request
        parameters.add("quantity", String.valueOf(request.getQuantity())); // Dynamic quantity
        parameters.add("total_amount", String.valueOf(request.getTotalAmount())); // Dynamic amount
        parameters.add("vat_amount", String.valueOf(request.getVatAmount())); // Dynamic VAT
        parameters.add("tax_free_amount", "0");
        parameters.add("green_deposit", "0");
        parameters.add("approval_url", request.getApprovalUrl()); // Dynamic URLs
        parameters.add("cancel_url", request.getCancelUrl());
        parameters.add("fail_url", request.getFailUrl());

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        System.out.println("//////////////////결제 했다!");

        return kakaoReady;
    }

    /**
     * 결제 완료 승인
     */
    public KakaoApproveResponse approveResponse(String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        return approveResponse;
    }

    /**
     * 결제 환불
     */
    public KakaoCancelResponse kakaoCancel() {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", "환불할 결제 고유 번호");
        parameters.add("cancel_amount", "환불 금액");
        parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
        parameters.add("cancel_vat_amount", "환불 부가세");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

        return cancelResponse;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}
