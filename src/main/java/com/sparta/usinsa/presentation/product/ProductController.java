package com.sparta.usinsa.presentation.product;

import com.sparta.usinsa.application.service.ProductService;
import com.sparta.usinsa.presentation.product.dto.product.ProductSearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductSearchResponse>> searchProducts(
            @RequestParam final String searchWord
    ) {
        final List<ProductSearchResponse> responses = productService.search(searchWord);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/products/search/top")
    public ResponseEntity<List<String>> searchProductsByTop() {
        final List<String> responses = productService.getTopPopularSearchKeywords();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
