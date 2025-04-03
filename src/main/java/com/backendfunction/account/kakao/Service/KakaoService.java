//package com.backendfunction.account.kakao.Service;
//
//import com.backendfunction.account.kakao.TokenDto.KakaoTokenResDto;
//import com.backendfunction.account.kakao.TokenDto.KakaoUserInfoResponseDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class KakaoService {
//
//    private String clientId;
//    private final String KAUTH_TOKEN_URL_HOST;
//    private final String KAUTH_USER_URL_HOST;
//
//    @Autowired
//    public KakaoService(@Value("${kakao.client_id}") String clientId) {
//        this.clientId = clientId;
//        KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com/";
//        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
//
//    }
//    public String getAccessTokenFromKakao(String code) {
//        KakaoTokenResDto kakaoTokenResDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https").path("/oauth/token")
//                        .queryParam("grant_type", "authorization_code")
//                        .queryParam("client_id", clientId)
//                        .queryParam("code", code)
//                        .build(true))
//                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
//                .retrieve()
//
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
//                .bodyToMono(KakaoTokenResDto.class).block();
//        log.info(" [Kakao Serveice] Access Token ------> {}", kakaoTokenResDto.getAccessToken());
//        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResDto.getRefreshToken());
//        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
//        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResDto.getIdToken());
//        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResDto.getScope());
//        return kakaoTokenResDto.getAccessToken();
//    }
//
//    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
//        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
//                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .path("/v2/user/me")
//                        .build(true))
//                .header(HttpHeaders.AUTHORIZATION, "bearer " + accessToken)
//                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
//                .retrieve()
//
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
//                .bodyToMono(KakaoUserInfoResponseDto.class)
//                .block();
//
//        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
//        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
//        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());
//        return userInfo;
//    }
//}
