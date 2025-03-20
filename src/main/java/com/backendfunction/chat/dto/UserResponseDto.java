package com.backendfunction.chat.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class UserResponseDto {
    public UserData data;
    @Data
    public static class UserData {
        public Long accountId;
        private String email;
        private String nickname;
        private String introduction;
        @Getter
        public String imgUrl;
    }
}
