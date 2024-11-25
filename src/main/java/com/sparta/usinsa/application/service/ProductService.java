package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.repository.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  // 상품 등록 (Create)
  public Product addProduct(Product product) {
    return productRepository.save(product);
  }

  // 상품 단건 조회 (Read)
  public Product getProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
  }

  // 상품 다건 조회 (사용자별) (Read)
  public List<Product> getProductsByUserId(Long userId) {
    return productRepository.findByUserId(userId);
  }

  // 상품 수정 (Update)
  public Product updateProduct(Long id, Product updatedProduct) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));

    // 상품 정보 업데이트
    product.setName(updatedProduct.getName());
    product.setPrice(updatedProduct.getPrice());
    product.setDescription(updatedProduct.getDescription());
    product.setProductUrl(updatedProduct.getProductUrl());
    product.setCategory(updatedProduct.getCategory());

    return productRepository.save(product);
  }

  // 상품 삭제 (Delete)
  public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
      throw new RuntimeException("Product not found");
    }
    productRepository.deleteById(id);
  }
}
