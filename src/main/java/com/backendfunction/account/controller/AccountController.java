package com.backendfunction.account.controller;

import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid AccountReqDto accountReqDto){
        return accountService.accountSignUp(accountReqDto);
    }

    @GetMapping("/api/demo-web")
    public List<String> Hello(){
        return Arrays.asList("리액트 스프링 ", "연결 성공");
    }
}
