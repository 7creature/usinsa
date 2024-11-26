package com.sparta.usinsa.presentation.common.config.security;


import com.sparta.usinsa.presentation.common.filter.AuthenticationExceptionHandlerFilter;
import com.sparta.usinsa.presentation.common.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final AuthenticationExceptionHandlerFilter authenticationExceptionHandlerFilter;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
        .formLogin(form -> form.disable()) // Form Login 비활성화
        .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 비활성화
        .authorizeRequests(
            (authorizeRequests) ->
                authorizeRequests
                    .requestMatchers("/auth/signup", "/auth/signin")
                    .permitAll()
                    .anyRequest()
                    .authenticated());
    http.sessionManagement(
        sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.addFilterBefore(
        authenticationExceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
