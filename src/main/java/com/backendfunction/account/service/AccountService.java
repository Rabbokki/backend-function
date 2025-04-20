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
import java.util.Optional;

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
        accountRepository.save(account);
        log.info("Account saved successfully: id={}", account.getId());

        return ResponseDto.success("회원가입 완료");
    }

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
        return new TokenDto(tokenDto) {
            public Long accountId = account.getId();
        };
    }

    public UserInfoDto getUserInfoByEmail(UserDetailsImpl userDetails) {
        if(userDetails == null) throw new RuntimeException("계정이 없습니다.");
        Account account = accountRepository.findById(userDetails.getAccount().getId())
                .orElseThrow(()-> new RuntimeException("계정이 없습니다."));
        return UserInfoDto.builder().account(account).build();
    }

//    public UserInfoDto getUserInfoByEmail(String email) {
//        Account account = accountRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("계정이 없습니다."));
//        return UserInfoDto.builder().account(account).build();
//    }

    public void updateUserInfo(String email, AccountReqDto accountReqDto) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("등록된 이매일 아닙니다."));

        if (accountReqDto.getEmail() != null && !accountReqDto.getEmail().equals(account.getEmail())) {
            Optional<Account> existingAccount = accountRepository.findByEmail(accountReqDto.getEmail());
            if (existingAccount.isPresent()) {
                throw new RuntimeException("이미 사용한 이매일 입니다.");
            }
            account.setEmail(accountReqDto.getEmail());
        }

        if (accountReqDto.getNickname() != null) {
            account.setNickname(accountReqDto.getNickname());
        }

        if (accountReqDto.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(accountReqDto.getPassword()));
        }

        accountRepository.save(account);
    }


    public void setHeader(HttpServletResponse response, TokenDto tokenDto){
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }


    public AccountDto findById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        return AccountDto.fromEntity(account);
    }

    public List<AccountDto> findAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(x -> AccountDto.fromEntity(x)).toList();
    }
    //로그아웃
    public ResponseDto<?> accountLogout(String email){
        RefreshToken refreshToken = refreshTokenRepository.findByAccountEmail(email).orElseThrow(
                ()-> new RuntimeException("리프레시 토큰 만료")
        );
        return ResponseDto.success("로그아웃 success");
    }

}
