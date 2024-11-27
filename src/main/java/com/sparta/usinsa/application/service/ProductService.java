package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import com.sparta.usinsa.presentation.product.dto.request.ProductRequestDto;
import com.sparta.usinsa.presentation.product.dto.response.ProductResponseDto;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  public ProductService(ProductRepository productRepository, UserRepository userRepository) {
    this.productRepository = productRepository;
    this.userRepository = userRepository;
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
  @Transactional
  public ProductResponseDto addProduct(ProductRequestDto productRequestDto, User user) {

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
    return new ProductResponseDto(productRepository.save(product));
  }

  // 상품 수정 (Update)
  @Transactional
  public Product updateProduct(Long id, ProductRequestDto productRequestDto) {
    // 기존 상품 조회
    Product existingProduct = productRepository.findById(id)
        .orElseThrow(() -> new CustomException("상품을 찾을 수 없습니다. ID: " + id, HttpStatus.NOT_FOUND));

    // 사용자 정보 직접 조회
    User user = userRepository.findById(productRequestDto.getUserId())
        .orElseThrow(
            () -> new CustomException("사용자를 찾을 수 없습니다. ID: " + productRequestDto.getUserId(),
                HttpStatus.NOT_FOUND));

    // 기존 객체를 수정하는 대신, 새로운 객체를 생성해서 반환
    return existingProduct.update(
        productRequestDto.getName(),
        productRequestDto.getPrice(),
        productRequestDto.getDescription(),
        productRequestDto.getProductUrl(),
        productRequestDto.getCategory(),
        user
    );
  }

  // 상품 삭제 (Delete)
  @Transactional
  public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
      throw new CustomException("상품을 찾을 수 없습니다. ID: " + id, HttpStatus.NOT_FOUND);
    }
    productRepository.deleteById(id);
  }
}
