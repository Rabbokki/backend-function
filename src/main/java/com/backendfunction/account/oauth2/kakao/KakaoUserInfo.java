package com.backendfunction.account.oauth2.kakao;

import com.backendfunction.account.oauth2.google.OAuth2UserInfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProviderId() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) ((Map)attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map)attributes.get("properties")).toString();
    }
}
