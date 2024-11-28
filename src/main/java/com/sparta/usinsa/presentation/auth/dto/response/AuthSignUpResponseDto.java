package com.sparta.usinsa.presentation.auth.dto.response;

import com.sparta.usinsa.presentation.auth.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthSignUpResponseDto {
  private Long id;
  private String email;
  private String name;
  private UserType type;

  @Builder
  public AuthSignUpResponseDto(Long id, String email, String name, UserType type) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.type = type;
  }
}
