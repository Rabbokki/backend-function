package com.backendfunction.account.oauth2.google;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
