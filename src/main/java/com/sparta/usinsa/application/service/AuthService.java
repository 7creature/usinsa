package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.auth.dto.request.AuthSignInRequestDto;
import com.sparta.usinsa.presentation.auth.dto.request.AuthSignUpRequestDto;
import com.sparta.usinsa.presentation.common.PasswordEncoder;
import com.sparta.usinsa.presentation.common.config.jwt.JwtHelper;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final JwtHelper jwtHelper;
  private final PasswordEncoder passwordEncoder;

  public void signup(AuthSignUpRequestDto authSignUpRequestDto, HttpServletResponse response) {

    if (userRepository.existsByEmail(authSignUpRequestDto.getEmail())) {
      throw new CustomException("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
    }

    User user = authSignUpRequestDto.toEntity(passwordEncoder);
    userRepository.save(user);

    // 회원가입 후 자동 로그인 처리
    AuthSignInRequestDto authSignInRequestDto = new AuthSignInRequestDto(
        authSignUpRequestDto.getEmail(), authSignUpRequestDto.getPassword());
    signin(authSignInRequestDto, response);
  }

  public void signin(AuthSignInRequestDto authSignInRequestDto, HttpServletResponse response) {
    User user = userRepository.findByEmail(authSignInRequestDto.getEmail())
        .orElseThrow(() -> new CustomException("USER_NOT_FOUND", HttpStatus.NOT_FOUND));

    // 비밀번호 검증
    authenticate(user, authSignInRequestDto.getPassword(), passwordEncoder);

    String accessToken = jwtHelper.createAccessToken(user);
    String refreshToken = jwtHelper.createRefreshToken(user);

    response.setHeader("Authorization", "Bearer " + accessToken);
    jwtHelper.setRefreshTokenCookie(response, refreshToken);
  }

  // 리프레시 토큰 검증
  public void refresh(HttpServletResponse response, String refreshToken) {
    jwtHelper.validate(refreshToken);
    String accessToken = jwtHelper.createAccessTokenFromRefreshToken(refreshToken);
    response.setHeader("Authorization", "Bearer " + accessToken);
  }

  public void authenticate(User user, String requestPassword, PasswordEncoder passwordEncoder) {
    if (user.isDeleted()) {
      throw new CustomException("탈퇴한 회원입니다.", HttpStatus.BAD_REQUEST);
    }
    if (!passwordEncoder.matches(requestPassword, user.getPassword())) {
      throw new CustomException("아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
  }
}