package com.sparta.usinsa.presentation.product.dto.response;

import com.sparta.usinsa.domain.entity.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {

  private Long id;
  private String name;
  private Long price;
  private String description;
  private String productUrl;
  private String category;
  @Getter
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;


  public ProductResponseDto(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.price = product.getPrice();
    this.description = product.getDescription();
    this.productUrl = product.getProductUrl();
    this.category = product.getCategory();
    this.createdAt = product.getCreatedAt();
    this.modifiedAt = product.getModifiedAt();
  }


  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }


  public static List<ProductResponseDto> fromProducts(List<Product> products) {
    return products.stream()
        .map(ProductResponseDto::new)
        .collect(Collectors.toList());
  }
}
