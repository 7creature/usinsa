package com.sparta.usinsa.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeStamped {
  @CreatedDate
  @Column(name = "created_at", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "modified_at")
  @Temporal(TemporalType.TIMESTAMP)
  protected LocalDateTime modifiedAt;

  @Column(name = "deleted_at")
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime deletedAt;

  public void delete() { this.deletedAt = LocalDateTime.now(); }

}
