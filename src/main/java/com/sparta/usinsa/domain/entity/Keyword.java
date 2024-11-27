package com.sparta.usinsa.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="keywords")
public class Keyword {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String keyword;
  private Long searchCount;
  private LocalDateTime lastSearched;

public Keyword(String keyword, Long searchCount, LocalDateTime lastSearched) {
  this.keyword = keyword;
  this.searchCount = searchCount;
  this.lastSearched = lastSearched;
}


}