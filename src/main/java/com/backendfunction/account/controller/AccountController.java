package com.backendfunction.account.controller;

import com.backendfunction.account.dto.AccountDto;
import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.dto.LoginReqDto;
import com.backendfunction.account.service.AccountService;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.jwt.dto.TokenDto;
import com.backendfunction.global.security.jwt.util.JwtUtil;
import com.backendfunction.global.security.user.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Slf4j
public class AccountController {
    private final JwtUtil jwtUtil;
    private final AccountService accountService;
    private final HttpServletRequest request;

    //회원가입
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<?> signUp(@RequestPart(value = "accountImg", required = false) List<MultipartFile> imgs,
                                 @RequestPart(value = "dto") @Valid AccountReqDto accountReqDto) {
        return ResponseDto.success(accountService.accountSignUp(accountReqDto,imgs));
    }

    //로그인
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        log.info("Login request: email={}, url={}, ip={}", loginReqDto.getEmail(), request.getRequestURL(), request.getRemoteAddr());
        try {
            TokenDto tokenDto = accountService.accountLogin(loginReqDto, response);
            log.info("Login success: email={}, accessToken={}", loginReqDto.getEmail(), tokenDto.getAccessToken());
            return ResponseDto.success(tokenDto);
        } catch (Exception e) {
            log.error("Login failed: email={}, error={}", loginReqDto.getEmail(), e.getMessage(), e);
            return ResponseDto.fail("LOGIN_FAILED", e.getMessage());
        }
    }
    //로그아웃
    @PostMapping("/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails)throws Exception{
        return ResponseDto.success(accountService.accountLogout(userDetails.getAccount().getEmail()));
    }

    // 내 정보 가져오기
    @GetMapping("/me")
    public ResponseDto<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(accountService.getUserInfoByEmail(userDetails.getAccount().getEmail()));
    }

    // 내 정보 수정하기
    @PutMapping("/me")
    public ResponseDto<?> updateUserInfo(@RequestHeader("Authorization") String token,
                                         @RequestBody @Valid AccountReqDto accountReqDto) {
        String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
        try {
            accountService.updateUserInfo(email, accountReqDto);
            return ResponseDto.success("수정 성공 했습니다.");
        } catch (RuntimeException e) {
            // If a runtime exception occurs (like "This email is already taken")
            return ResponseDto.fail("EMAIL_ALREADY_TAKEN", e.getMessage());
        }
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
