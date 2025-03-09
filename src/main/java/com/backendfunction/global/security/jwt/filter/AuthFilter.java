package com.backendfunction.global.security.jwt.filter;

import com.backendfunction.global.dto.GlobalResDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("AUTH");
        String accountValue = request.getHeader("ACCOUNT-VALUE");

        if(auth!=null && auth.equals("true")){
            if(accountValue == null){
                jwtExceptionHandler(response, "AccessToken 만료", HttpStatus.BAD_REQUEST);
                return;
            }
            setAuthentication(accountValue);
        }
        filterChain.doFilter(request,response);
    }
    public void setAuthentication(String email){
        Authentication authentication = createAuthentication(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Authentication createAuthentication(String email){
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status){
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDto(msg, status.value()));
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
