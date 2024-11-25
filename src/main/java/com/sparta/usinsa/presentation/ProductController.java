package com.sparta.usinsa.presentation;

import com.sparta.usinsa.application.service.ProductService;
import com.sparta.usinsa.domain.entity.Product;
import java.util.List;
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

  @Autowired
  private ProductService productService;

  // 상품 등록
  @PostMapping
  public ResponseEntity<Product> addProduct(@RequestBody Product product) {
    Product savedProduct = productService.addProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
  }

  // 상품 단건 조회
  @GetMapping("/{id}")
  public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    Product product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

  // 상품 다건 조회
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Product>> getProductsByUser(@PathVariable Long userId) {
    List<Product> products = productService.getProductsByUserId(userId);
    return ResponseEntity.ok(products);
  }

  // 상품 수정
  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable Long id,
      @RequestBody Product product) {
    Product updatedProduct = productService.updateProduct(id, product);
    return ResponseEntity.ok(updatedProduct);
  }

  // 상품 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
