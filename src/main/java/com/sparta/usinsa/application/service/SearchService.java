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

}
