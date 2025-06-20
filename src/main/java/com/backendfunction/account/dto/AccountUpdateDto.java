package com.backendfunction.account.dto;

import com.backendfunction.account.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDto {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식을 확인해 주세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String imgUrl;

    public static AccountUpdateDto fromEntity(Account account) {
        return new AccountUpdateDto(
                account.getEmail(),
                account.getNickname(),
                account.getBirthday(),
                account.getImgUrl()
        );
    }

    @JsonIgnore
    public boolean isAdult() {
        if (birthday == null) {
            return false;
        }
        return birthday.isBefore(LocalDate.now().minusYears(19));
    }
}
