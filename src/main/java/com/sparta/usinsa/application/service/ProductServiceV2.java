package com.sparta.usinsa.application.service;

import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceV2 {

  private final StringRedisTemplate redisTemplate;
  private static final String VIEW_COUNT_KEY = "product:viewcount:";
  private static final String TOP_PRODUCTS_KEY = "product:top";

  // 상품 조회 시 조회수 증가
  @Transactional
  public void viewProduct(Long productId, String userId) {
    String viewCountKey = VIEW_COUNT_KEY + productId;

    // 어뷰징 방지: 동일 사용자의 중복 조회 제한
    String userViewKey = viewCountKey + ":user:" + userId;
    if (Boolean.TRUE.equals(redisTemplate.hasKey(userViewKey))) {
      return; // 이미 조회한 사용자라면 조회수를 증가시키지 않음
    }

    // 조회수 증가
    redisTemplate.opsForValue().increment(viewCountKey);

    // 중복 조회 방지를 위한 사용자 키 설정 (1시간 유지)
    redisTemplate.opsForValue().set(userViewKey, "1", 1, TimeUnit.HOURS);

    // 인기 상품 랭킹 업데이트
    redisTemplate.opsForZSet().incrementScore(TOP_PRODUCTS_KEY, productId.toString(), 1);
  }

  public Long getProductViewCount(Long productId) {
  }

  public Set<String> getTopRankedProducts(int limit) {
  }

  public void resetViewCounts() {

  }
}
