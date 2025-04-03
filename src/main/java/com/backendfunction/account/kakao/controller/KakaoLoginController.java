package com.backendfunction.account.kakao.controller;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.kakao.Service.KakaoUserService;
import com.backendfunction.account.kakao.TokenDto.KakaoTokenResDto;
import com.backendfunction.account.kakao.TokenDto.KakaoUserInfoResponseDto;
import com.backendfunction.account.kakao.TokenDto.UserRequest;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.global.security.jwt.dto.TokenDto;
import com.backendfunction.global.security.jwt.util.JwtUtil;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
    private final AccountRepository repository;
    private final JwtUtil jwtUtil;

//    @GetMapping("/callback")
//    public ResponseEntity<?> callback(@RequestPart("code") String code) throws IOException {
//        String accessToken = kakaoService.getAccessTokenFromKakao(code);
//        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PostMapping("/user")
    public ResponseEntity<?> getKakaoUser(@RequestBody UserRequest userRequest) {
        KakaoTokenResDto tokenDto = userRequest.getTokenDto();
        KakaoUserInfoResponseDto userDto = userRequest.getUserDto();
        System.out.println(tokenDto.toString());
        System.out.println(userDto.toString());
        System.out.println(tokenDto.getRefreshToken());
        System.out.println(userDto.kakaoAccount.getEmail());
        System.out.println(userDto.getKakaoAccount().getProfile().getNickName());
        String email = userDto.kakaoAccount.getEmail();
        String nickName = userDto.getKakaoAccount().getProfile().getNickName();
        String kakaoRefreshToken = tokenDto.getRefreshToken();
        Long kakaoId = userDto.id;
        Account account = repository.findByEmail(email).orElseGet(() -> {
            Account user = new Account();
            user.setEmail(email);
            user.setNickname(nickName);
            user.setKakaoId(kakaoId);
            return user;
        });
        String accessToken = jwtUtil.createToken(email, "Access");
        TokenDto tokenDto1 = new TokenDto(accessToken, kakaoRefreshToken);
        Map<String, Object> repons = new HashMap<>();
        repons.put("account", account);
        repons.put("token", tokenDto1);

        return ResponseEntity.ok(repons);

    }

}
