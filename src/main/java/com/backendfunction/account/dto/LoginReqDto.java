package com.backendfunction.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginReqDto {
    private String email;
    private String password;

    public void setEncodePwd(String encodePwd){
        this.password = encodePwd;
    }
}
