package com.backendfunction.account.service;

import com.backendfunction.account.dto.AccountDto;
import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.account.dto.LoginReqDto;
import com.backendfunction.account.dto.UserInfoDto;
import com.backendfunction.account.entity.Account;
import com.backendfunction.account.entity.RefreshToken;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.account.repository.RefreshTokenRepository;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.jwt.dto.TokenDto;
import com.backendfunction.global.security.jwt.util.JwtUtil;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.s3.S3Service;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    @Transactional
    public ResponseDto<?> accountSignUp(AccountReqDto accountReqDto, List<MultipartFile> files) {
        log.info("Starting sign-up for email: {}", accountReqDto.getEmail());
        if (accountRepository.findByEmail(accountReqDto.getEmail()).isPresent()) {
            log.warn("Email already exists: {}", accountReqDto.getEmail());
            return ResponseDto.fail("EMAIL_ALREADY_TAKEN", "이미 사용 중인 이메일입니다.");
        }
        if (!accountReqDto.isAdult()) {
            log.warn("User is not adult: birthday={}", accountReqDto.getBirthday());
            return ResponseDto.fail("AGE_RESTRICTION", "만 19세 이상만 가입할 수 있습니다.");
        }
        if (files != null && !files.isEmpty()) {
            try {
                log.info("Uploading file to S3: fileName={}", files.get(0).getOriginalFilename());
                String imgUrl = s3Service.uploadFile(files.get(0));
                accountReqDto.setImgUrl(imgUrl);
                log.info("S3 upload successful: imgUrl={}", imgUrl);
            } catch (Exception e) {
                log.error("S3 upload failed", e);
                return ResponseDto.fail("S3_UPLOAD_FAILED", "이미지 업로드 실패: " + e.getMessage());
            }
        }

        accountReqDto.setEncodePwd(passwordEncoder.encode(accountReqDto.getPassword()));
        Account account = new Account(accountReqDto);
        log.info("Saving account to DB: email={}", account.getEmail());
        Account savedAccount = accountRepository.save(account);
        log.info("Account saved successfully: id={}", savedAccount.getId());

        String accessToken = jwtUtil.createAccessToken(accountReqDto.getEmail());
        return ResponseDto.success(Map.of(
                "message", "회원가입 완료",
                "accessToken", accessToken,
                "accountId", savedAccount.getId()
        ));
    }

    @Transactional
    public TokenDto accountLogin(LoginReqDto loginReqDto, HttpServletResponse response) {
        log.info("Querying account: email={}", loginReqDto.getEmail());
        Account account = accountRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(() -> {
            log.error("Account not found: email={}", loginReqDto.getEmail());
            return new RuntimeException("계정이 없습니다.");
        });
        log.info("Verifying password: email={}", loginReqDto.getEmail());
        if (!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
            log.error("Password mismatch: email={}", loginReqDto.getEmail());
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        log.info("Generating tokens for email: {}", loginReqDto.getEmail());
        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getEmail());
        log.info("Tokens created: accessToken={}", tokenDto.getAccessToken());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(loginReqDto.getEmail());
        if (refreshToken.isPresent()) {
            log.info("Updating existing refresh token for email: {}", loginReqDto.getEmail());
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            log.info("Creating new refresh token for email: {}", loginReqDto.getEmail());
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getEmail());
            refreshTokenRepository.save(newToken);
        }
        log.info("Setting response headers for email: {}", loginReqDto.getEmail());
        setHeader(response, tokenDto);
        return new TokenDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken(), account.getId());
    }

    public UserInfoDto getUserInfoByEmail(String email) {
        if (email == null || email.isEmpty()) {
            log.error("Email not provided");
            throw new IllegalArgumentException("이메일이 제공되지 않았습니다.");
        }
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Account not found: email={}", email);
                    return new RuntimeException("계정이 없습니다: " + email);
                });
        return UserInfoDto.builder().account(account).build();
    }

    @Transactional
    public void updateUserInfo(String email, AccountReqDto accountReqDto) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Account not found: email={}", email);
                    return new RuntimeException("등록된 이메일이 아닙니다.");
                });

        if (accountReqDto.getEmail() != null && !accountReqDto.getEmail().equals(account.getEmail())) {
            Optional<Account> existingAccount = accountRepository.findByEmail(accountReqDto.getEmail());
            if (existingAccount.isPresent()) {
                log.error("Email already taken: {}", accountReqDto.getEmail());
                throw new RuntimeException("이미 사용 중인 이메일입니다.");
            }
            account.setEmail(accountReqDto.getEmail());
        }

        if (accountReqDto.getNickname() != null) {
            account.setNickname(accountReqDto.getNickname());
        }

        if (accountReqDto.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(accountReqDto.getPassword()));
        }

        log.info("Updating account: email={}", account.getEmail());
        accountRepository.save(account);
        log.info("Account updated successfully: email={}", account.getEmail());
    }

    public void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    public AccountDto findById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Account not found: id={}", id);
                    return new RuntimeException("계정이 없습니다: id=" + id);
                });
        return AccountDto.fromEntity(account);
    }

    public List<AccountDto> findAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public ResponseDto<?> accountLogout(String email) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccountEmail(email)
                .orElseThrow(() -> {
                    log.error("Refresh token not found: email={}", email);
                    return new RuntimeException("리프레시 토큰 만료");
                });
        refreshTokenRepository.delete(refreshToken);
        log.info("Logout successful: email={}", email);
        return ResponseDto.success("로그아웃 성공");
    }
}