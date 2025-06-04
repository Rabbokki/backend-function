package com.backendfunction.global.security.jwt.util;

import com.backendfunction.account.entity.RefreshToken;
import com.backendfunction.account.repository.RefreshTokenRepository;
import com.backendfunction.global.security.jwt.dto.TokenDto;
import com.backendfunction.global.security.user.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Lazy
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final long ACCESS_TIME = 24 * 60 * 60 * 1000L;
    private static final long REFRESH_TIME = 24 * 60 * 60 * 2000L;
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
        log.info("JwtUtil initialized with secret key");
    }

    public String getHeaderToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        log.info("Header {} value: {}", headerName, token);
        return token;
    }

    public TokenDto createAllToken(String email) {
        return new TokenDto(createToken(email, "Access"), createToken(email, "Refresh"));
    }

    public String createAccessToken(String email) {
        return createToken(email, "Access");
    }

    public String createToken(String email, String type) {
        Date date = new Date();
        long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
        log.info("Generated {} token for email: {}", type, email);
        return token;
    }

    public boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            log.info("Token validated successfully: {}", token);
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = claims.getSubject();
        log.info("Extracted email from token: {}", email);
        return email;
    }

    public Boolean refreshTokenValidation(String token) {
        if (!tokenValidation(token)) {
            return false;
        }
        String email = getEmailFromToken(token);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(email);
        boolean isValid = refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
        log.info("Refresh token validation for email: {}, isValid: {}", email, isValid);
        return isValid;
    }

    public Authentication createAuthentication(String email) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            log.info("Created authentication for email: {}", email);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (Exception e) {
            log.error("Failed to create authentication for email: {}, error: {}", email, e.getMessage());
            return null;
        }
    }
}
