package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.product.dto.product.ProductSearchResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;

    public List<ProductSearchResponse> search(final String searchWord) {
        final String redisKey = "search:" + searchWord;
        redisTemplate.opsForZSet().incrementScore(redisKey, searchWord, 1);

        final List<Product> products = productRepository.searchByKeyword(searchWord);
        return ProductSearchResponse.from(products);
    }

    public List<String> getTopPopularSearchKeywords() {
        final Set<String> keys = redisTemplate.keys("search:*");
        final List<String> topKeywords = new ArrayList<>();

        for (String key : keys) {
            Set<ZSetOperations.TypedTuple<String>> entries = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 9);
            for (ZSetOperations.TypedTuple<String> entry : entries) {
                topKeywords.add(entry.getValue());
            }
        }

        return topKeywords.stream()
                .distinct()
                .limit(10)
                .toList();
    }
}
