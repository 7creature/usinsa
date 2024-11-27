package com.sparta.usinsa.domain.repository;

import com.sparta.usinsa.domain.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByCategory(String category);

  @Query("SELECT p FROM Product p " +
          "JOIN FETCH p.user u " +
          "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
          "   OR LOWER(u.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<Product> searchByKeyword(@Param("keyword") String keyword);
}
