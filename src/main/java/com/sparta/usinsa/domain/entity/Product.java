package com.sparta.usinsa.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String name;
  private Long price;
  private String description;
  private String productUrl;
  private String category;

  protected Product() {
  }


  public Product(
      User user,
      String name,
      Long price,
      String description,
      String productUrl,
      String category) {

    this.user = user;
    this.name = name;
    this.price = price;
    this.description = description;
    this.productUrl = productUrl;
    this.category = category;
  }
  
  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public String getName() {
    return name;
  }

  public Long getPrice() {
    return price;
  }

  public String getDescription() {
    return description;
  }

  public String getProductUrl() {
    return productUrl;
  }

  public String getCategory() {
    return category;
  }

  public void updateProduct(
      String name,
      Long price,
      String description,
      String productUrl,
      String category) {

    this.name = name;
    this.price = price;
    this.description = description;
    this.productUrl = productUrl;
    this.category = category;
  }
}
