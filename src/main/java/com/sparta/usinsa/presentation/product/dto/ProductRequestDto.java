package com.sparta.usinsa.presentation.product.dto;

import lombok.Getter;

@Getter
public class ProductRequestDto {

  private String name;
  private Long price;
  private String description;
  private String productUrl;
  private String category;

  public ProductRequestDto() {
  }

  public ProductRequestDto(
      String name,
      Long price,
      String description,
      String productUrl,
      String category
  ) {

    this.name = name;
    this.price = price;
    this.description = description;
    this.productUrl = productUrl;
    this.category = category;
  }

}
