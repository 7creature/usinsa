package com.sparta.usinsa.domain.entity;

import com.sparta.usinsa.presentation.auth.UserType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
public class User extends TimeStamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  private String brand;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserType type;

  @Builder
  public User(String email, String password, String name, String brand, UserType type) {
    if (type == UserType.OWNER && (brand == null || brand.isBlank())) {
      throw new IllegalArgumentException("OWNER는 브랜드명을 필수로 입력해야 합니다.");
    }
    if (type != UserType.OWNER && brand != null) {
      throw new IllegalArgumentException("USER는 브랜드명을 가질 수 없습니다.");
    }
    this.email = email;
    this.password = password;
    this.name = name;
    this.brand = brand;
    this.type = type;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updatePassword(Long requestUserId, String currentPassword, String changePassword, PasswordEncoder passwordEncoder) {
    validateUserIdentity(requestUserId);

    if(!passwordEncoder.matches(currentPassword, this.getPassword())) {
      throw new RuntimeException("비밀번호가 틀렸습니다.");
    }
    if(!passwordEncoder.matches(changePassword, this.getPassword())) {
      throw new RuntimeException("현재 사용중인 비밀 번호입니다.");
    }
    this.password = passwordEncoder.encode(changePassword);
  }

  @Override
  public void delete() {
    this.name = ("deleted_" + this.getName() + this.getId());
    super.delete();
  }

  public void authenticate(String requestPassword, PasswordEncoder passwordEncoder) {
    if(this.getDeletedAt() != null) {
      throw new RuntimeException("탈퇴한 회원입니다.");
    }
    if(!passwordEncoder.matches(requestPassword, this.getPassword())) {
      throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
  }

  public void validateUserIdentity(Long requestUserId) {
    if (!this.id.equals(requestUserId)) {
      throw new RuntimeException("본인이 아닌 사용자입니다.");
    }
  }

  public boolean isOwner() { return this.type == UserType.OWNER; }

  public void validateUserPassword(String requestPassword, PasswordEncoder passwordEncoder) {
    if(!passwordEncoder.matches(requestPassword, this.getPassword())) {
      throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
    }
  }
}
