package com.backendfunction.account.controller;

import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid AccountReqDto accountReqDto){
        return accountService.accountSignUp(accountReqDto);
    }
}
