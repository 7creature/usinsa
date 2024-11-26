package com.sparta.usinsa.presentation.auth;

import com.sparta.usinsa.application.service.AuthService;
import com.sparta.usinsa.presentation.auth.dto.request.AuthSignInRequestDto;
import com.sparta.usinsa.presentation.auth.dto.request.AuthSignUpRequestDto;
import com.sparta.usinsa.presentation.auth.dto.response.AuthSignUpResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.sparta.usinsa.presentation.common.config.jwt.JwtHelper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtHelper jwtHelper;

  @PostMapping("/signup")
  public ResponseEntity<AuthSignUpResponseDto> signUp(
      @Valid @RequestBody AuthSignUpRequestDto authRequestDto, HttpServletResponse response) {
    AuthSignUpResponseDto authResponseDto = authService.signUp(authRequestDto);

    // 회원가입시 자동로그인
    String token =
        authService.signIn(authResponseDto.getEmail(), authRequestDto.getPassword());
    jwtHelper.addTokenToCookie(response, token);
    return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);
  }

  @PostMapping("/signin")
  public ResponseEntity<Void> signIn(
      @Valid @RequestBody AuthSignInRequestDto authRequestDto, HttpServletResponse response) {
    String token =
        authService.signIn(authRequestDto.getEmail(), authRequestDto.getPassword());
    jwtHelper.addTokenToCookie(response, token);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
