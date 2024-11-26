package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.auth.UserType;
import com.sparta.usinsa.presentation.auth.dto.request.AuthSignUpRequestDto;
import com.sparta.usinsa.presentation.auth.dto.response.AuthSignUpResponseDto;
import com.sparta.usinsa.presentation.common.config.jwt.JwtHelper;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtHelper jwtHelper;

  @Transactional
  public AuthSignUpResponseDto signUp(AuthSignUpRequestDto authSignUpRequestDto) {
    authSignUpRequestDto.validateBrand();
    this.isValidateUserUniqueness(authSignUpRequestDto.getEmail(), authSignUpRequestDto.getName());
    User user =
        User.builder()
            .email(authSignUpRequestDto.getEmail())
            .password(passwordEncoder.encode(authSignUpRequestDto.getPassword()))
            .name(authSignUpRequestDto.getName())
            .type(UserType.valueOf(authSignUpRequestDto.getType()))
            .brand(authSignUpRequestDto.getBrand())
            .build();
    userRepository.save(user);
    return AuthSignUpResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .type(user.getType())
        .build();
  }

  private void isValidateUserUniqueness(String email, String name) {
    Optional<User> foundUser = userRepository.findByEmailOrNameIncludingDeleted(email, name);
    if (foundUser.isPresent()) {
      User existingUser = foundUser.get();
      if (existingUser.getEmail().equals(email)) {
        throw new CustomException("EMAIL_DUPLICATED", HttpStatus.CONFLICT);
      }
      if (existingUser.getName().equals(name)) {
        throw new CustomException("USERNAME_DUPLICATED", HttpStatus.CONFLICT);
      }
    }
  }

  public String signIn(String email, String password) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException("EMAIL_NOT_FOUND", HttpStatus.NOT_FOUND));

    user.authenticate(password, passwordEncoder);
    return jwtHelper.generateAccessToken(user.getId(), user.getEmail(), user.getType());
  }

}
