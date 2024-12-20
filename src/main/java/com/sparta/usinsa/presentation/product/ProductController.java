package com.sparta.usinsa.presentation.product;

import com.sparta.usinsa.application.service.ProductService;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.common.annotation.AuthUser;
import com.sparta.usinsa.presentation.product.dto.request.ProductRequestDto;
import com.sparta.usinsa.presentation.product.dto.response.ProductResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  // 상품 등록
  @PostMapping
  public ResponseEntity<ProductResponseDto> addProduct(
      @RequestBody ProductRequestDto productRequestDto, @AuthUser User user) {

    // Product 객체 생성
    ProductResponseDto product = productService.addProduct(productRequestDto, user);

    // Product 저장
    return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }

  // 상품 단건 조회
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
    Product product = productService.getProductById(id);
    return ResponseEntity.ok(new ProductResponseDto(product));
  }

  // 상품 다건 조회 (사용자별)
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ProductResponseDto>> getProductsByUser(@PathVariable Long userId) {
    List<Product> products = productService.getProductsByUserId(userId);
    List<ProductResponseDto> productDtos = products.stream()
        .map(ProductResponseDto::new)
        .collect(Collectors.toList());
    return ResponseEntity.ok(productDtos);
  }

  // 상품 수정
  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
      @RequestBody ProductRequestDto productRequestDto, @AuthUser User user) {

    Product updatedProduct = productService.updateProduct(id, productRequestDto, user);

    return ResponseEntity.ok(new ProductResponseDto(updatedProduct));
  }

  // 상품 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
