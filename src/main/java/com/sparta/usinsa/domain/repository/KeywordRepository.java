package com.sparta.usinsa.domain.repository;

import com.sparta.usinsa.domain.entity.Keyword;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByKeyword(final String searchWord);
}
