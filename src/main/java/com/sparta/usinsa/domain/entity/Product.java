package com.sparta.usinsa.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private String description;

    private String productUrl;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Product(
            String name,
            Long price,
            String description,
            String productUrl,
            String category,
            User user
    ) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.productUrl = productUrl;
        this.category = category;
        this.user = user;
    }

    // 상품 수정 메서드
    public Product update(
            String name,
            Long price,
            String description,
            String productUrl,
            String category,
            User user
    ) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.productUrl = productUrl;
        this.category = category;
        this.user = user;
        return this;
    }
}
