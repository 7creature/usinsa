package com.sparta.usinsa.presentation.auth;

import com.sparta.usinsa.application.service.AuthService;
import com.sparta.usinsa.presentation.auth.dto.request.AuthSignInRequestDto;
import com.sparta.usinsa.presentation.auth.dto.request.AuthSignUpRequestDto;
import com.sparta.usinsa.presentation.common.config.jwt.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtHelper jwtHelper;

  // 회원가입 API
  @PostMapping("/signup")
  public void signup(@RequestBody AuthSignUpRequestDto authSignUpRequestDto, HttpServletResponse response) {
    authService.signup(authSignUpRequestDto, response);
  }

  // 로그인 API
  @PostMapping("/signin")
  public void signin(@RequestBody AuthSignInRequestDto authSignInRequestDto, HttpServletResponse response) {
    authService.signin(authSignInRequestDto, response);
  }

  // 리프레시 토큰 API
  @PostMapping("/refresh")
  public void refresh(@RequestParam String refreshToken, HttpServletResponse response) {
    authService.refresh(response, refreshToken);
  }
}
