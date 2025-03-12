package com.backendfunction.global.security;

import com.backendfunction.global.security.jwt.filter.JwtAuthFilter;
import com.backendfunction.global.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }
    //cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns( Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*", "POST", "GET", "DELETE", "PUT", "PATCH"));  //프론트에서 보내는 CRUD 허용
        configuration.setAllowedHeaders(Arrays.asList("*", "Access_Token")); // 프론트에서 보내는 모든 해더 허용
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader ( "*" );
        configuration.addExposedHeader("Access_Token"); // Axios 에서는 이런식으로 Access_token으로 지정해줘야 보임 *사용불가

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())  // CORS 설정
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/account/signup").permitAll()
                        .requestMatchers("/account/**").permitAll()
                        .requestMatchers("/file/**").permitAll()
                        .anyRequest().authenticated())
                .securityContext(securityContext -> securityContext.requireExplicitSave(false)) // SecurityContext 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless 설정
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)  // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
                .build();
    }
}
