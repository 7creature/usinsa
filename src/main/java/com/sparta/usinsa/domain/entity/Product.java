package com.sparta.usinsa.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Long price;
  private String description;
  private String productUrl;
  private String category;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public Product() {
  }


  public Product(
      Long id,
      String name,
      Long price,
      String description,
      String productUrl,
      String category,
      User user) {

    this.id = id;
    this.name = name;
    this.price = price;
    this.description = description;
    this.productUrl = productUrl;
    this.category = category;
    this.user = user;
  }


  public Product(
      String name,
      Long price,
      String description,
      String productUrl,
      String category,
      User user) {
    this.name = name;
    this.price = price;
    this.description = description;
    this.productUrl = productUrl;
    this.category = category;
    this.user = user;
  }


  public Product update(
      String name,
      Long price,
      String description,
      String productUrl,
      String category,
      User user) {

    return new Product(
        this.id,
        name,
        price,
        description,
        productUrl,
        category,
        user);
  }
}
