package com.sparta.usinsa.presentation.common.config.filter;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.common.config.jwt.JwtHelper;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import com.sparta.usinsa.presentation.common.config.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtHelper jwtHelper;
  private final UserRepository userRepository;
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    String jwt = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
    }

    // JWT 토큰이 있는 경우 검증 및 인증 설정
    if (jwt != null) {
      try {
        logger.info("JWT 토큰을 검증합니다.");
        Claims claims = jwtHelper.getUserInfoFromToken(jwt);
        Long userId = Long.parseLong(claims.getSubject());
        logger.info("유저 ID: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException("USER_NOT_FOUND", HttpStatus.NOT_FOUND));

        // UserPrincipal을 사용하여 Security Context에 인증 정보 설정
        UserPrincipal userPrincipal = new UserPrincipal(user);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("인증 정보가 SecurityContext에 설정되었습니다: {}", authentication);
      } catch (JwtException | IllegalArgumentException e) {
        logger.error("유효하지 않은 JWT 토큰: {}", e.getMessage());
        throw new CustomException("INVALID_TOKEN", HttpStatus.UNAUTHORIZED);
      }
    } else {
      logger.info("JWT 토큰이 제공되지 않았습니다.");
    }

    chain.doFilter(request, response);
  }
}
