package com.backendfunction.account.service;

import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public ResponseEntity<?> accountSignUp(AccountReqDto accountReqDto) {
        Account account = new Account(accountReqDto);
        Account save = accountRepository.save(account);

        return ResponseEntity.ok(save);
    }
}
