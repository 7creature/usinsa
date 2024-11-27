package com.sparta.usinsa.presentation.product.dto.product;

import com.sparta.usinsa.domain.entity.Product;
import java.util.List;

public record ProductSearchResponse(
        Long id,
        String brand,
        String productName,
        Long price
) {
    public static ProductSearchResponse from(final Product product) {
        return new ProductSearchResponse(product.getId(), product.getUser().getBrand(), product.getName(),
                product.getPrice());
    }

    public static List<ProductSearchResponse> from(final List<Product> products) {
        return products.stream()
                .map(ProductSearchResponse::from)
                .toList();
    }
}
