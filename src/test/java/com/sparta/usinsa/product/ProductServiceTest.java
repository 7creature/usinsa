package com.sparta.usinsa.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.usinsa.application.service.ProductService;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.domain.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private UserRepository userRepository;

  private User testUser;

  @BeforeEach
  void setUp() {
    // 테스트용 User 생성
    testUser = new User();
    testUser.setUsername("testUser");
    testUser.setPassword("password123");
    userRepository.save(testUser);
  }

  @Test
  void testAddProduct() {

    // 상품 추가 테스트
    Product product = new Product(
        testUser,
        "Test Product",
        100L,
        "Test Description",
        "http://example.com",
        "Electronics");

    Product savedProduct = productService.addProduct(product);

    // 저장된 상품 검증
    assertNotNull(savedProduct.getId());
    assertEquals("Test Product", savedProduct.getName());
    assertEquals(100L, savedProduct.getPrice());
  }

  @Test
  void testGetProductById() {

    // 상품 단건 조회 테스트
    Product product = new Product(
        testUser,
        "Test Product",
        100L,
        "Test Description",
        "http://example.com",
        "Electronics");

    Product savedProduct = productService.addProduct(product);

    Product foundProduct = productService.getProductById(savedProduct.getId());

    // 조회된 상품 검증
    assertNotNull(foundProduct);
    assertEquals(savedProduct.getId(), foundProduct.getId());
  }

  @Test
  void testGetProductsByUserId() {

    // 사용자별 (브랜드별) 다건 조회 테스트
    Product product1 = new Product(
        testUser,
        "Product 1",
        100L,
        "Description 1",
        "http://example1.com",
        "Electronics");

    Product product2 = new Product(
        testUser,
        "Product 2",
        200L,
        "Description 2",
        "http://example2.com",
        "Furniture");

    productService.addProduct(product1);
    productService.addProduct(product2);

    List<Product> products = productService.getProductsByUserId(testUser.getId());

    // 조회된 상품 검증
    assertEquals(2, products.size());
  }

  @Test
  void testUpdateProduct() {

    // 상품 수정 테스트
    Product product = new Product(
        testUser,
        "Test Product",
        100L,
        "Test Description",
        "http://example.com",
        "Electronics");

    Product savedProduct = productService.addProduct(product);

    // 수정된 상품 정보 업데이트하기
    savedProduct.updateProduct(
        "Updated Product",
        200L,
        "Updated Description",
        "http://updated.com",
        "Updated Category");

    // 업데이트
    Product updatedProduct = productService.updateProduct(savedProduct.getId(), savedProduct);

    // 수정된 상품 검증
    assertEquals("Updated Product", updatedProduct.getName());
    assertEquals(200L, updatedProduct.getPrice());
  }

  @Test
  void testDeleteProduct() {
    // 상품 삭제 테스트
    Product product = new Product(testUser,
        "Test Product",
        100L,
        "Test Description",
        "http://example.com",
        "아우터");

    Product savedProduct = productService.addProduct(product);

    // 상품 삭제
    productService.deleteProduct(savedProduct.getId());

    // 삭제된 상품 조회 시 예외 발생
    assertThrows(RuntimeException.class, () -> productService.getProductById(savedProduct.getId()));
  }
}
