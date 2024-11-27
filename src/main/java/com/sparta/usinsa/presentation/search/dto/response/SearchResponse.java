package com.sparta.usinsa.presentation.search.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponse {

  private Long id;
  private String brandname;
  private String productname;
  private Long price;

  public SearchResponse(Long id, String brandname, String productname, Long price) {
    this.id = id;
    this.brandname = brandname;
    this.productname = productname;
    this.price = price;
  }

}
