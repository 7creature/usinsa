package com.sparta.usinsa.presentation.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  // redis 연결을 위한 Connection 생성
  // 구성된 RedisTemplate 를 통해서 데이터 통신으로 처리되는 직렬화 수행
  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory(){
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();

    // redis 연결
    template.setConnectionFactory(connectionFactory);

    // 기본 직렬화
    template.setDefaultSerializer(new StringRedisSerializer());

    // key-value 직렬화
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());

    // hash key-value 직렬화
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new StringRedisSerializer());

    // json 직렬화
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

    return template;
  }
}
