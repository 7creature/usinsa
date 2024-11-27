package com.sparta.usinsa.presentation.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.*;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.sparta.usinsa.domain.repository")
@EnableTransactionManagement // 트랜잭션 관리 활성화
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setPackagesToScan("com.sparta.usinsa.domain.entity");  // 엔티티 클래스가 있는 패키지 설정
    factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    factoryBean.setJpaProperties(hibernateProperties());
    return factoryBean;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  private Properties hibernateProperties() {
    Properties properties = new Properties();
    properties.put("hibernate.hbm2ddl.auto", "update");  // DDL 자동 처리 방식 (create, update, validate 등)
    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");  // 사용하는 DB에 맞는 dialect 설정
    properties.put("hibernate.show_sql", "true");  // 쿼리 출력
    properties.put("hibernate.format_sql", "true");  // 쿼리 포맷팅
    properties.put("hibernate.use_sql_comments", "true");  // 쿼리에 대한 설명 출력
    return properties;
  }
}

