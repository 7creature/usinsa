package com.sparta.usinsa.domain.entity;

import com.sparta.usinsa.presentation.auth.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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

  public void updatePassword(String password) {
    this.password = password;
  }

  @Override
  public void delete() {
    this.name = ("deleted_" + this.getName() + this.getId());
    super.delete();
  }

  public boolean isDeleted() {
    return this.getDeletedAt() != null;
  }
}
