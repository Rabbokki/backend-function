package com.backendfunction.global.security.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Long accountId;

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenDto(String accessToken, String refreshToken, Long accountId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accountId = accountId;
    }

    public TokenDto(TokenDto tokenDto) {
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
        this.accountId = tokenDto.getAccountId();
    }
}
