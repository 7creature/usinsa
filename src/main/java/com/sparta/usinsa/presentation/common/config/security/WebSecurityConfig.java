package com.sparta.usinsa.presentation.common.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())  // csrf() 설정을 람다 방식으로 변경
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/auth/signup", "/auth/signin", "/auth/refresh").permitAll()  // 요청 패턴을 지정
            .anyRequest().authenticated())  // 나머지 요청은 인증 필요
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // 세션 관리를 상태 없는 방식으로 설정

    return http.build();
  }
}
