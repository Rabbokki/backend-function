package com.backendfunction.account.dto;

import com.backendfunction.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private String email;
    private String nickname;

    public static AccountDto fromEntity(Account account) {
        return new AccountDto(
                account.getId(),
                account.getEmail(),
                account.getNickname()
        );
    }

    public static Account fromDto(AccountDto dto) {
        Account account = new Account();
        account.setId(dto.getId());
        account.setEmail(dto.getEmail());
        account.setNickname(dto.getNickname());
        return account;
    }
}