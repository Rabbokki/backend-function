package com.backendfunction.account.oauth2.google;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class KakaoUserDetail implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        return (String) response.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("properties");
        return (String) response.get("nickname");
    }

}
