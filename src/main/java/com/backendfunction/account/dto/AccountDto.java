package com.backendfunction.account.dto;

import com.backendfunction.Cart.dto.CartDto;
import com.backendfunction.account.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private List<CartDto> dtos = new ArrayList<>();

    public static AccountDto fromEntity(Account account) {
        return new AccountDto(
                account.getId(),
                account.getEmail(),
                account.getNickname(),
                account.getPassword(),
                account.getCarts().stream().map(x -> CartDto.fromEntity(x)).toList()
        );
    }

    public static Account fromDto(AccountDto dto) {
        Account account = new Account();
        account.setId(dto.getId());
        account.setEmail(dto.getEmail());
        account.setNickname(dto.getNickname());
        account.setPassword(dto.getPassword());
        return account;
    }


}