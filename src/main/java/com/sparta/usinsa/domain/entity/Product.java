package com.sparta.usinsa.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
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

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime modifiedAt;

  protected Product() {
  }

  public Product(
      User user,
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
    this.user = user;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }


  // 명시적 Setter
  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setProductUrl(String productUrl) {
    this.productUrl = productUrl;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
