package com.backendfunction.account.controller;

import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.dto.LoginReqDto;
import com.backendfunction.account.service.AccountService;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    //회원가입
    @PostMapping("/signup")
    public ResponseDto<?> signUp(@RequestBody @Valid AccountReqDto accountReqDto){
        return ResponseDto.success(accountService.accountSignUp(accountReqDto));
    }
    //로그인
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response){
        return ResponseDto.success(accountService.accountLogin(loginReqDto, response));
    }



    @GetMapping("/api/demo-web")
    public List<String> Hello(){
        return Arrays.asList("리액트 스프링 ", "연결 성공");
    }
}
