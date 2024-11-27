package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.search.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private ProductRepository repository;

  public Page<SearchResponse> searches(int page, int size, String keyword) {
    Pageable pageable = PageRequest.of(page - 1, size);

    Page<Product> products = repository.findAllByNameContaining(pageable, keyword);

    if (products.isEmpty()) {
      products = repository.findAllByCategory(pageable, keyword);
    }

    return products
        .map(product -> new SearchResponse(
            product.getId(),
            product.getUser().getBrand(),
            product.getName(),
            product.getPrice()));
  }
}
