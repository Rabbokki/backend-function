package com.backendfunction.account.controller;

import com.backendfunction.account.dto.AccountDto;
import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.dto.LoginReqDto;
import com.backendfunction.account.service.AccountService;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseDto<?> signUp(@RequestBody @Valid AccountReqDto accountReqDto) {
        return ResponseDto.success(accountService.accountSignUp(accountReqDto));
    }

    //로그인
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        return ResponseDto.success(accountService.accountLogin(loginReqDto, response));
    }


    @GetMapping("/api/demo-web")
    public List<String> Hello() {
        return Arrays.asList("리액트 스프링 ", "연결 성공");
    }

    @GetMapping("/api/account")
    public ResponseEntity<?> findAllAccount() {
        List<AccountDto> accountDtos = accountService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(accountDtos);
    }
//id로 검색하기
    @GetMapping("/api/account/id/{id}")
    public ResponseEntity<?> findByAccountId(@PathVariable("id") Long id) {
        AccountDto dto = accountService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

}
