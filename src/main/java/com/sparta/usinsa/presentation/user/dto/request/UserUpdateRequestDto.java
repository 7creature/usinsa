package com.sparta.usinsa.presentation.user.dto.request;

import com.sparta.usinsa.presentation.common.annotation.ValidPasswordPatten;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

  @NotBlank(message = "이름을 입력해주세요.")
  private String name;

  @NotBlank(message = "현재 비밀번호를 입력해주세요.")
  private String currentPassword;

  @NotBlank(message = "새 비밀번호를 입력해주세요.")
  @ValidPasswordPatten
  private String newPassword;
}