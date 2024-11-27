package com.sparta.usinsa;

import com.sparta.usinsa.presentation.common.config.JpaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@Import(JpaConfig.class) // JpaConfig를 수동으로 임포트
public class UsinsaApplication {

  public static void main(String[] args) {
    SpringApplication.run(UsinsaApplication.class, args);
  }

}
