package com.backendfunction.account.kakao.TokenDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private KakaoTokenResDto tokenDto;
    private KakaoUserInfoResponseDto userDto;
}
