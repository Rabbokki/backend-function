//package com.backendfunction.account.oauth2.google;
//
//import com.backendfunction.account.entity.Account;
//import com.backendfunction.account.repository.AccountRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class GoogleUserDetailsServiceImpl implements UserDetailsService {
//    private final AccountRepository accountRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Account account = accountRepository.findByEmail(email)
//                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//        return new CustomUserDetails(account);
//    }
//}
