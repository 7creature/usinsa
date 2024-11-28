package com.sparta.usinsa.presentation.common.config.jwt;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {

  private final long accessTokenExpiration = 1000L * 60 * 30;
  private final long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 7;
  private final SecretKey secretKey;
  private final UserRepository userRepository;

  public JwtHelper(UserRepository userRepository)
  {
    this.userRepository = userRepository;
    this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }

  public String createAccessToken(User user) {
    return Jwts.builder()
        .setSubject(String.valueOf(user.getId()))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public String createRefreshToken(User user) {
    return Jwts.builder()
        .setSubject(String.valueOf(user.getId()))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public String createAccessTokenFromRefreshToken(String refreshToken) {
    Claims claims = validate(refreshToken);
    // claims.getSubject()가 email이라 가정하고 조회
    User user = userRepository.findByEmail(claims.getSubject())
        .orElseThrow(() -> new CustomException("USER_NOT_FOUND", HttpStatus.NOT_FOUND));

    return createAccessToken(user);
  }

  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public Claims validate(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException("INVALID_TOKEN", HttpStatus.UNAUTHORIZED);
    }
  }

  public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setMaxAge((int) (refreshTokenExpiration / 1000));
    refreshTokenCookie.setPath("/");
    response.addCookie(refreshTokenCookie);
  }
}