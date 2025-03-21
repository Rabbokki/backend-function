package com.backendfunction.account.dto;

import com.backendfunction.account.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountReqDto {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식을 확인해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*()_+=-])(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9!@#$%^&*()_+=-]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String imgUrl;

    public static AccountReqDto fromEntity(Account account) {
        return new AccountReqDto(
                account.getEmail(),
                null,
                account.getNickname(),
                account.getBirthday(),
                account.getImgUrl()
        );
    }

    public void setEncodePwd(String encodePwd){
        this.password = encodePwd;
    }
    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
    @AssertTrue(message = "만 19세 이상만 가입할 수 있습니다.")
    @JsonIgnore
    public boolean isAdult() {
        if (birthday == null) {
            return false; // 생일이 없으면 검증 실패
        }
        LocalDate now = LocalDate.now();
        return birthday.isBefore(now.minusYears(19)); // 현재 날짜 기준 19년 전보다 이전인지 확인
    }


}