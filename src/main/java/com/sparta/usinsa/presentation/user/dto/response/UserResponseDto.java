package com.sparta.usinsa.presentation.user.dto.response;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.auth.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
  private Long id;
  private String email;
  private String nickname;
  private String brand;
  private UserType type;

  public UserResponseDto(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.nickname = user.getName();
    this.brand = user.getBrand();
    this.type = user.getType();
  }
}