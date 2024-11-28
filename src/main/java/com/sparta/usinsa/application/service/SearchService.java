package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Keywords;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.repository.KeywordRepository;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.search.dto.response.SearchResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final ProductRepository productRepository;
  private final KeywordRepository keywordRepository;

  @Transactional
  public Page<SearchResponse> searches(int page, int size, String keyword) {
    Pageable pageable = PageRequest.of(page - 1, size);

    Page<Product> products = productRepository.findAllByNameContaining(pageable, keyword);

    if (products.isEmpty()) {
      products = productRepository.findAllByCategory(pageable, keyword);
    }

   popularKeyword(keyword);

    return products
        .map(product -> new SearchResponse(
            product.getId(),
            product.getUser().getBrand(),
            product.getName(),
            product.getPrice()));
  }

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
