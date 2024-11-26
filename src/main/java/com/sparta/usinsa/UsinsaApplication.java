package com.sparta.usinsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UsinsaApplication {

  public static void main(String[] args) {
    SpringApplication.run(UsinsaApplication.class, args);
  }

}
