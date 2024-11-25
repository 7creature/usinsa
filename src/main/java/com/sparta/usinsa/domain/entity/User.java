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

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserType type;

  @Builder
  public User(String email, String password, String name, UserType type) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.type = type;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updatePassword(Long reqUserId, String currentPassword, String changePassword, PasswordEncoder passwordEncoder) {
    validateUserIdentity(reqUserId);

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

  public void validateUserIdentity(Long reqUserId) {
    if (!this.id.equals(reqUserId)) {
      throw new RuntimeException("본인이 아닌 사용자입니다.");
    }
  }
}
