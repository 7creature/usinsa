package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Keywords;
import com.sparta.usinsa.domain.repository.KeywordRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PopularKeywordService {

  private KeywordRepository keywordRepository;

  @Transactional
  public void popularKeyword(String keyword) {
    Optional<Keywords> optionalKeyword = keywordRepository.findByKeyword(keyword);
    // Optional Wrapper 클레스로 null값 입력 방지

    if (optionalKeyword.isPresent()) { // isPresent()으로 null값인지 확인
      Keywords keywords = optionalKeyword.get();
      keywords.setSearchCount(keywords.getSearchCount() + 1); // 중복된 키워드 카운트
      keywords.setLastSearched(LocalDateTime.now());
    }else { // null이면 생성
      Keywords newKeyword = new Keywords(keyword, 1L);
      newKeyword.setLastSearched(LocalDateTime.now());
      keywordRepository.save(newKeyword);
    }
  }
}
