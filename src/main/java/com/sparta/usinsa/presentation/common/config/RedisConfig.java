package com.sparta.usinsa.presentation.common.config;

import java.time.Duration;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

  @Value("${spring.data.redis.port}")
  private int port;

  @Value("${spring.data.redis.host}")
  private String host;

  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

    return template;
  }

  @Bean
  public CacheManager redisManager(RedisConnectionFactory connectionFactory) {
    RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60)) // ttl 1시간
            .disableCachingNullValues())// null값 캐싱하지 않음
        .transactionAware() // 트렌젝션과 함게 레디스 동작
        .build();
    return cacheManager;
  }
}
