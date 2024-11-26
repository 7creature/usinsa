package com.sparta.usinsa.presentation.product;

import com.sparta.usinsa.application.service.ProductService;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.product.dto.ProductRequestDto;
import com.sparta.usinsa.presentation.product.dto.ProductResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductController {

  private final ProductService productService;
  private final UserService userService;

  @Autowired
  public ProductController(ProductService productService, UserService userService) {
    this.productService = productService;
    this.userService = userService;
  }

  // 상품 등록
  @PostMapping
  public ResponseEntity<ProductResponseDto> addProduct(
      @RequestBody ProductRequestDto productRequestDto) {

    User user = userService.getUserById(productRequestDto.getUserId());

    // Product 객체 생성
    Product product = new Product(
        productRequestDto.getName(),
        productRequestDto.getPrice(),
        productRequestDto.getDescription(),
        productRequestDto.getProductUrl(),
        productRequestDto.getCategory(),
        user // User를 함께 저장
    );

    // Product 저장
    Product savedProduct = productService.addProduct(product);

    return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponseDto(savedProduct));
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
  public ResponseEntity<ProductResponseDto> updateProduct(
      @PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {

    User user = userService.getUserById(productRequestDto.getUserId());
    
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
