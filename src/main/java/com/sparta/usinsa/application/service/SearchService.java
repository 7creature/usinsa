package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Keywords;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.repository.KeywordRepository;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.search.dto.response.KeywordResponse;
import com.sparta.usinsa.presentation.search.dto.response.SearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private ProductRepository productRepository;
  private KeywordRepository keywordRepository;

  private PopularKeywordService popularKeywordService;

  public Page<SearchResponse> searches(int page, int size, String keyword) {
    Pageable pageable = PageRequest.of(page - 1, size);

    Page<Product> products = productRepository.findAllByNameContaining(pageable, keyword);

    if (products.isEmpty()) {
      products = productRepository.findAllByCategory(pageable, keyword);
    }

    popularKeywordService.popularKeyword(keyword);

    return products
        .map(product -> new SearchResponse(
            product.getId(),
            product.getUser().getBrand(),
            product.getName(),
            product.getPrice()));
  }

  public List<KeywordResponse> V1PopularSearch() {
    List<Keywords> keywords = keywordRepository.findTop10ByOrderBySearchCountDesc();

    return keywords
        .stream()
        .map(keyword -> new KeywordResponse(
        keyword.getId(),
        keyword.getKeyword(),
        keyword.getSearchCount(),
        keyword.getLastSearched()))
        .toList();
  }

  @Cacheable(value = "popularSearch", key = "'list'", cacheManager = "caffeineManager")
  public List<KeywordResponse> V2PopularSearch() {
    List<Keywords> keywords = keywordRepository.findTop10ByOrderBySearchCountDesc();

    return keywords
        .stream()
        .map(keyword -> new KeywordResponse(
            keyword.getId(),
            keyword.getKeyword(),
            keyword.getSearchCount(),
            keyword.getLastSearched()))
        .toList();
  }
}
