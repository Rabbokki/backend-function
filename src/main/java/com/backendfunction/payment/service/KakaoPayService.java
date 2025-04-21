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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
    private final PostRepository postRepository;
    private final AccountService accountService;
    @Value("${kakao.pay.admin-key}")
    private String adminKey;
    @Value("${kakao.pay.ready-url}")
    private String readyUrl;
    @Value("${kakao.pay.approve-url}")
    private String approveUrl;
    @Value("${kakao.pay.cancel-url}")
    private String cancelUrl;
    static final String cid = "TC0ONETIME";
    private KakaoReadyResponse kakaoReady;

    public KakaoReadyResponse kakaoPayReady(KakaoReadyRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        Object principal = authentication.getPrincipal();
        String email;
        log.info("Principal type: {}, value: {}", principal.getClass(), principal);
        if (principal instanceof UserDetailsImpl) {
            email = ((UserDetailsImpl) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new IllegalStateException("알 수 없는 principal 타입: " + principal.getClass());
        }

        UserInfoDto userInfo = accountService.getUserInfoByEmail(email);
        Long accountId = userInfo.getAccountId();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", request.getPostId().toString());
        parameters.add("partner_user_id", accountId.toString());
        parameters.add("item_name", request.getItemName());
        parameters.add("quantity", String.valueOf(request.getQuantity()));
        parameters.add("total_amount", String.valueOf(request.getTotalAmount()));
        parameters.add("vat_amount", String.valueOf(request.getVatAmount()));
        parameters.add("tax_free_amount", "0");
        parameters.add("green_deposit", "0");
        parameters.add("approval_url", request.getApprovalUrl());
        parameters.add("cancel_url", request.getCancelUrl());
        parameters.add("fail_url", request.getFailUrl());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                readyUrl,
                requestEntity,
                KakaoReadyResponse.class);

        if (kakaoReady != null) {
            kakaoReady.setPartnerOrderId(request.getPostId().toString());
        }

        return kakaoReady;
    }

    public KakaoApproveResponse approveResponse(String pgToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }
        String email;
        Object principal = authentication.getPrincipal();
        log.info("Principal type: {}, value: {}", principal.getClass(), principal);
        if (principal instanceof UserDetailsImpl) {
            email = ((UserDetailsImpl) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new IllegalStateException("알 수 없는 principal 타입: " + principal.getClass());
        }

        UserInfoDto userInfo = accountService.getUserInfoByEmail(email);
        Long accountId = userInfo.getAccountId();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", kakaoReady.getPartnerOrderId());
        parameters.add("partner_user_id", accountId.toString());
        parameters.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(
                approveUrl,
                requestEntity,
                KakaoApproveResponse.class);
    }

    public KakaoCancelResponse kakaoCancel(String tid, int cancelAmount, int cancelTaxFreeAmount, int cancelVatAmount) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", tid);
        parameters.add("cancel_amount", String.valueOf(cancelAmount));
        parameters.add("cancel_tax_free_amount", String.valueOf(cancelTaxFreeAmount));
        parameters.add("cancel_vat_amount", String.valueOf(cancelVatAmount));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(
                cancelUrl,
                requestEntity,
                KakaoCancelResponse.class);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }
}
