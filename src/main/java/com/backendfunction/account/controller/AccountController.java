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
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Slf4j
public class AccountController {
    private final JwtUtil jwtUtil;
    private final AccountService accountService;
    private final HttpServletRequest request;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<?> signUp(@RequestPart(value = "accountImg", required = false) List<MultipartFile> imgs,
                                 @RequestPart(value = "dto") @Valid AccountReqDto accountReqDto) {
        log.info("Signup request: email={}, ip={}", accountReqDto.getEmail(), request.getRemoteAddr());
        try {
            ResponseDto<?> response = accountService.accountSignUp(accountReqDto, imgs);
            log.info("Signup success: email={}", accountReqDto.getEmail());
            return response;
        } catch (Exception e) {
            log.error("Signup failed: email={}, error={}", accountReqDto.getEmail(), e.getMessage(), e);
            return ResponseDto.fail("SIGNUP_FAILED", "회원가입 실패: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        log.info("Login request: email={}, url={}, ip={}", loginReqDto.getEmail(), request.getRequestURL(), request.getRemoteAddr());
        try {
            TokenDto tokenDto = accountService.accountLogin(loginReqDto, response);
            log.info("Login success: email={}, accessToken={}, accountId={}", loginReqDto.getEmail(), tokenDto.getAccessToken(), tokenDto.getAccountId());
            return ResponseDto.success(Map.of(
                    "accessToken", tokenDto.getAccessToken(),
                    "refreshToken", tokenDto.getRefreshToken(),
                    "accountId", tokenDto.getAccountId()
            ));
        } catch (Exception e) {
            log.error("Login failed: email={}, error={}", loginReqDto.getEmail(), e.getMessage(), e);
            return ResponseDto.fail("LOGIN_FAILED", e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            ResponseDto<?> response = accountService.accountLogout(userDetails.getAccount().getEmail());
            log.info("Logout success: email={}", userDetails.getAccount().getEmail());
            return response;
        } catch (Exception e) {
            log.error("Logout failed: email={}, error={}", userDetails.getAccount().getEmail(), e.getMessage(), e);
            return ResponseDto.fail("LOGOUT_FAILED", e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseDto<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String email = userDetails.getUsername();
        try {
            ResponseDto<?> response = ResponseDto.success(accountService.getUserInfoByEmail(email));
            log.info("Fetched user info: email={}", email);
            return response;
        } catch (Exception e) {
            log.error("Get user info failed: email={}, error={}", email, e.getMessage(), e);
            return ResponseDto.fail("USER_NOT_FOUND", e.getMessage());
        }
    }

    @PutMapping("/me")
    public ResponseDto<?> updateUserInfo(@RequestHeader("Authorization") String token,
                                         @RequestBody @Valid AccountReqDto accountReqDto) {
        String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
        try {
            accountService.updateUserInfo(email, accountReqDto);
            log.info("Updated user info: email={}", email);
            return ResponseDto.success("수정 성공 했습니다.");
        } catch (RuntimeException e) {
            log.error("Update user info failed: email={}, error={}", email, e.getMessage(), e);
            return ResponseDto.fail("EMAIL_ALREADY_TAKEN", e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> findAllAccount() {
        List<AccountDto> accountDtos = accountService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(accountDtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AccountDto> findByAccountId(@PathVariable("id") Long id) {
        AccountDto dto = accountService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
