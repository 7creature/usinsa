package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Keywords;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.repository.KeywordRepository;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.search.dto.response.KeywordResponse;
import com.sparta.usinsa.presentation.search.dto.response.SearchResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {


  private final ProductRepository productRepository;
  private final KeywordRepository keywordRepository;


  private final RedisTemplate<String, Object> redisTemplate;

  private static final String SEARCH_KEY = "popular:search";

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

  public List<KeywordResponse> V1PopularSearch() {
    List<Keywords> keywords = keywordRepository.findTop10ByOrderBySearchCountDesc();

    return keywords.stream().map(
        keyword -> new KeywordResponse(keyword.getId(), keyword.getKeyword(),
            keyword.getSearchCount(), keyword.getLastSearched())).toList();
  }

  @Cacheable(value = SEARCH_KEY)
  public List<KeywordResponse> V2PopularSearch() {
    ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

    Set<Object> popularKeyword = zSetOperations.reverseRange(SEARCH_KEY, 0, 9);

    if (popularKeyword.isEmpty() || popularKeyword == null) {
      List<Keywords> keywords = keywordRepository.findTop10ByOrderBySearchCountDesc();

      return keywords.stream().map(
          keyword -> new KeywordResponse(keyword.getId(), keyword.getKeyword(),
              keyword.getSearchCount(), keyword.getLastSearched())).toList();
    }

    return popularKeyword.stream().map(keyword -> {
      Optional<Keywords> optionalKeywords = keywordRepository.findByKeyword((String) keyword);
      if (optionalKeywords.isPresent()) {
        Keywords keywords = optionalKeywords.get();
        return new KeywordResponse(keywords.getId(), keywords.getKeyword(),
            keywords.getSearchCount(), keywords.getLastSearched());
      }
      return null;
    }).filter(Objects::nonNull).toList();

  }

  @Transactional
  public void popularKeyword(String keyword) {
    ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
    Double score = zSetOperations.incrementScore(SEARCH_KEY, keyword, 1);

    Optional<Keywords> optionalKeyword = keywordRepository.findByKeyword(keyword);
    // Optional Wrapper 클레스로 null값 입력 방지

    if (optionalKeyword.isPresent()) { // isPresent()으로 null값인지 확인
      Keywords keywords = optionalKeyword.get();
      keywords.setSearchCount(score.longValue()); // 중복된 키워드 카운트
      keywords.setLastSearched(LocalDateTime.now());
    } else { // null이면 생성
      Keywords newKeyword = new Keywords(keyword, 1L);
      newKeyword.setLastSearched(LocalDateTime.now());
      keywordRepository.save(newKeyword);
    }
  }


}
