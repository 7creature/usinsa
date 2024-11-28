package com.sparta.usinsa.presentation.common.config.security;

import com.sparta.usinsa.presentation.common.config.filter.JwtAuthenticationFilter;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecretKey secretKey() {
    return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())  // 버전업으로 인한 csrf() 설정을 람다 방식으로 변경
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/signup", "/auth/signin", "/auth/refresh").permitAll()
            .requestMatchers("/api/products/**").hasRole("OWNER")
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/auth/signin")
            .permitAll())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
