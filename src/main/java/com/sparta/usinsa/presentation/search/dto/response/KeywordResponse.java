package com.sparta.usinsa.presentation.search.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeywordResponse {
  private Long id;
  private String keyword;
  private Long searchCount;
  private LocalDateTime lastSearched;

  public KeywordResponse(Long id, String keyword, Long searchCount, LocalDateTime lastSearched) {
    this.id = id;
    this.keyword = keyword;
    this.searchCount = searchCount;
    this.lastSearched = lastSearched;
  }

}
