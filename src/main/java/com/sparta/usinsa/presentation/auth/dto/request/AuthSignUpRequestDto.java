package com.sparta.usinsa.presentation.auth.dto.request;


import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.auth.UserType;
import com.sparta.usinsa.presentation.common.PasswordEncoder;
import com.sparta.usinsa.presentation.common.annotation.ValidEnum;
import com.sparta.usinsa.presentation.common.annotation.ValidPasswordPatten;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
public class AuthSignUpRequestDto {

  @Email
  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Size(min = 8, max = 20, message = "최소 8글자 최대 20글자로 입력해주세요.")
  @ValidPasswordPatten
  private String password;

  @NotBlank(message = "이름을 입력해주세요.")
  private String name;

  @ValidEnum(enumClass = UserType.class, message = "주어진 타입중에서 선택해주세요.")
  @NotBlank(message = "가입형태를 지정 해주세요.")
  private String type;

  private String brand;

  @Builder
  public AuthSignUpRequestDto(String email, String password, String name, String type,
      String brand) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.type = type;
    this.brand = brand;
  }

  // 브랜드명이 OWNER일 때만 필수로 입력되도록 체크
  public boolean isOwner() {
    return UserType.OWNER.name().equals(type);
  }

  // 요청 시 브랜드 검증
  public void validateBrand() {
    if (isOwner() && (brand == null || brand.isBlank())) {
      throw new IllegalArgumentException("OWNER는 브랜드명을 필수로 입력해야 합니다.");
    }
    if (!isOwner() && brand != null && !brand.isBlank()) {
      throw new IllegalArgumentException("USER는 브랜드명을 가질 수 없습니다.");
    }
  }

  // AuthSignUpRequestDto -> User 엔티티로 변환
  public User toEntity(PasswordEncoder passwordEncoder) {
    return User.builder()
        .email(this.email)
        .password(passwordEncoder.encode(this.password))  // 비밀번호 암호화
        .name(this.name)
        .type(UserType.valueOf(this.type))  // 타입은 UserType enum으로 변환
        .brand(this.brand)
        .build();
  }
}
