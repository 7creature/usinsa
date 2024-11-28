package com.sparta.usinsa.domain.repository;

import com.sparta.usinsa.domain.entity.Keywords;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KeywordRepository extends JpaRepository<Keywords,Long> {

  Optional<Keywords> findByKeyword(String keyword);


  List<Keywords> findTop10ByOrderBySearchCountDesc();
}
