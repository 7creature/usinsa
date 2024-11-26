package com.sparta.usinsa.presentation.common.config.jwt;


import com.sparta.usinsa.presentation.auth.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j(topic = "JWT Helper : ")
@Component
public class JwtHelper {
  @Value("${jwt.secret.key}")
  private String secretKey;

  @Value("${token-expires-in}")
  private long expiresIn;

  private Key key;

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(Long userId, String email, UserType userType) {
    return BEARER_PREFIX
        + Jwts.builder()
        .setSubject(userId.toString())
        .claim("email", email)
        .claim("userType", userType)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiresIn))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // HttpServletResponse Cookie 에 토큰삽입 후 전달.
  public void addTokenToCookie(HttpServletResponse response, String token) {
    try {
      token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

      Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
      cookie.setPath("/");
      response.addCookie(cookie);
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage());
      throw new RuntimeException("서버 오류가 발생했습니다.");
    }
  }

  public void validate(String accessToken) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody()
          .getSubject();
    } catch (JwtException e) {
      throw new RuntimeException("유효하지 않은 로그인 토큰 입니다.");
    }
  }

  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}
