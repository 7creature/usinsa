package com.sparta.usinsa.domain.repository;

import com.sparta.usinsa.domain.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByUserId(Long userId);
}
