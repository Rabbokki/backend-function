package com.backendfunction.account.kakao.controller;

import com.backendfunction.account.kakao.Service.KakaoService;
import com.backendfunction.account.kakao.TokenDto.KakaoUserInfoResponseDto;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {
    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.redirect_uri}")
    private String redirect_uri;
    private final KakaoService kakaoService;


    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestPart("code") String code) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<?> getKakaoUser(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        System.out.println(code);
        String tokenUrl = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code" +
                "&client_id=" + client_id +
                "&redirect_uri=" + redirect_uri +
                "&code=" + code;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, null, String.class);
        String responseBody = response.getBody();
        System.out.println("카카오 응답" + responseBody);
        return null;
    }
}
