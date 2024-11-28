package com.sparta.usinsa.presentation.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
public class CacheConfig {

  @Bean
  public CacheManager caffeineManager() {
    CaffeineCacheManager caffeineManager = new CaffeineCacheManager("popularSearch");
    caffeineManager.setCaffeine(Caffeine.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(60, TimeUnit.MINUTES));
    return caffeineManager;
  }

}
