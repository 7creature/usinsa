package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import com.sparta.usinsa.presentation.product.dto.ProductRequestDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final UserService userService;

  public ProductService(ProductRepository productRepository, UserService userService) {
    this.productRepository = productRepository;
    this.userService = userService;
  }

  // 상품 단건 조회 (Read)
  public Product getProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new CustomException("상품을 찾을 수 없습니다. ID: " + id, HttpStatus.NOT_FOUND));
  }

  // 상품 다건 조회 (사용자별)
  public List<Product> getProductsByUserId(Long userId) {
    return productRepository.findByUserId(userId);
  }

  // 상품 등록 (Create)
  public Product addProduct(ProductRequestDto productRequestDto) {
    User user = userService.getUserById(productRequestDto.getUserId());
    if (user == null) {
      throw new CustomException("사용자를 찾을 수 없습니다. ID: " + productRequestDto.getUserId(),
          HttpStatus.NOT_FOUND);
    }

    // Product 객체 생성
    Product product = new Product(
        productRequestDto.getName(),
        productRequestDto.getPrice(),
        productRequestDto.getDescription(),
        productRequestDto.getProductUrl(),
        productRequestDto.getCategory(),
        user
    );

    // 상품 저장
    return productRepository.save(product);
  }

  // 상품 수정 (Update)
  public Product updateProduct(Long id, ProductRequestDto productRequestDto) {
    // 기존 상품 조회
    Product existingProduct = productRepository.findById(id)
        .orElseThrow(() -> new CustomException("상품을 찾을 수 없습니다. ID: " + id, HttpStatus.NOT_FOUND));

    // 사용자 정보 조회
    User user = userService.getUserById(productRequestDto.getUserId());
    if (user == null) {
      throw new CustomException("사용자를 찾을 수 없습니다. ID: " + productRequestDto.getUserId(),
          HttpStatus.NOT_FOUND);
    }

    existingProduct.setName(productRequestDto.getName());
    existingProduct.setPrice(productRequestDto.getPrice());
    existingProduct.setDescription(productRequestDto.getDescription());
    existingProduct.setProductUrl(productRequestDto.getProductUrl());
    existingProduct.setCategory(productRequestDto.getCategory());
    existingProduct.setUser(user);

    return productRepository.save(existingProduct);
  }

  // 상품 삭제 (Delete)
  public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
      throw new CustomException("상품을 찾을 수 없습니다. ID: " + id, HttpStatus.NOT_FOUND);
    }
    productRepository.deleteById(id);
  }
}
