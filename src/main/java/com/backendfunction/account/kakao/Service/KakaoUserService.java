package com.backendfunction.account.kakao.Service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.kakao.TokenDto.KakaoTokenResDto;
import com.backendfunction.account.kakao.TokenDto.KakaoUserInfoResponseDto;
import com.backendfunction.account.repository.AccountRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoUserService {



//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthenticationException {
//        OAuth2User auth2User = super.loadUser(userRequest);
//        return auth2User;
////        return new CustomUserDetails(account,oAuth2User.getAttributes());
//    }




}
