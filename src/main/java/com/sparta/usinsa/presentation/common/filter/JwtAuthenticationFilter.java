package com.sparta.usinsa.presentation.common.filter;

import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.sparta.usinsa.presentation.common.config.jwt.JwtHelper;
import com.sparta.usinsa.domain.entity.User;

@Order(1)
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtHelper jwtHelper;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (this.isApplicable(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    // Request 에서 쿠키를 읽어 JWT 토큰 추출
    String accessToken = extractTokenFromCookies(request);
    jwtHelper.validate(accessToken);
    Authentication authentication = getAuthentication(accessToken);
    // SecurityContext 에 인증 정보 설정
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  public boolean isApplicable(HttpServletRequest request) {
    return request.getRequestURI().startsWith("/auth/signup") || request.getRequestURI()
        .startsWith("/auth/signin");
  }

  public String extractTokenFromCookies(HttpServletRequest request) {
    return Arrays.stream(
            Optional.ofNullable(request.getCookies())
                .orElseThrow(() -> new CustomException("NOT_FOUND_ACCESS_TOKEN", HttpStatus.NOT_FOUND)))
        .filter(cookie -> "Authorization".equals(cookie.getName()))
        .findFirst()
        .map(
            cookie -> {
              try {
                // URL 디코딩 수행
                String token = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                // Bearer 접두사 제거
                if (token.startsWith("Bearer ")) {
                  return token.substring(7);
                }
                return token;
              } catch (UnsupportedEncodingException e) {
                throw new CustomException("INVALID_ACCESS_TOKEN", HttpStatus.UNAUTHORIZED);
              }
            })
        .orElseThrow(() -> new CustomException("NOT_FOUND_ACCESS_TOKEN", HttpStatus.NOT_FOUND));
  }

  private Authentication getAuthentication(String token) {
    Claims claims = jwtHelper.getUserInfoFromToken(token);
    Long id = Long.valueOf(claims.getSubject());

    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new CustomException("USER_NOT_FOUND", HttpStatus.NOT_FOUND));

    List<SimpleGrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(user.getType().toString()));
    return new UsernamePasswordAuthenticationToken(user, token, authorities);
  }
}

