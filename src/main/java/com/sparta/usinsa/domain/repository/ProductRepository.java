package com.sparta.usinsa.domain.repository;

import com.sparta.usinsa.domain.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByCategory(String category);

  Page<Product> findAllByNameContaining(Pageable pageable,String name);

  Page<Product> findAllByCategory(Pageable pageable, String category);

  Optional<List<Product>> findByUserId(Long userId);

}
