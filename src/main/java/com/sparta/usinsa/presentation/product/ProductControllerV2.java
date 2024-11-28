package com.sparta.usinsa.presentation.product;

import com.sparta.usinsa.application.service.ProductServiceV2;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductControllerV2 {

  private final ProductServiceV2 productServiceV2;

  // 상품 조회
  @PostMapping("/{productId}/view")
  public ResponseEntity<String> viewProduct(@PathVariable Long productId, @RequestParam String userId) {
    productServiceV2.viewProduct(productId, userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .build();
  }

  // 조회수 가져오기
  @GetMapping("/{productId}/views")
  public ResponseEntity<Long> getProductViewCount(@PathVariable Long productId) {
    Long viewCount = productServiceV2.getProductViewCount(productId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(viewCount);
  }

  // 인기 게시글 랭킹 조회
  @GetMapping("/ranking")
  public ResponseEntity<Set<String>> getTopRankedProducts(@RequestParam(defaultValue = "10") int limit) {
    Set<String> topProducts = productServiceV2.getTopRankedProducts(limit);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(topProducts);
  }
}
