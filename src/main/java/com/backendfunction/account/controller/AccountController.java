package com.backendfunction.account.controller;

import com.backendfunction.account.dto.AccountDto;
import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.dto.LoginReqDto;
import com.backendfunction.account.service.AccountService;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.jwt.util.JwtUtil;
import com.backendfunction.global.security.user.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        System.out.println("called");
        return ResponseDto.success(accountService.accountSignUp(accountReqDto));
    }

    //로그인
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        return ResponseDto.success(accountService.accountLogin(loginReqDto, response));
    }
    //로그아웃
    @PostMapping("/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails)throws Exception{
        return ResponseDto.success(accountService.accountLogout(userDetails.getAccount().getEmail()));
    }

    // 내 정보 가져오기
    @GetMapping("/me")
    public ResponseDto<?> getUserInfo(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
        return accountService.getUserInfoByEmail(email);
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to Spring Boot!";
    }

    @GetMapping("/test")
    public String test() {
        return "Test endpoint works!";
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
