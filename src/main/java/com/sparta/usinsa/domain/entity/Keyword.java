package com.sparta.usinsa.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "keywords")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private Long searchCount = 0L;

    @LastModifiedDate
    private LocalDateTime lastSearched;

    public Keyword(final String keyword) {
        this.keyword = keyword;
    }

    public Keyword(final LocalDateTime lastSearched) {
        this.lastSearched = lastSearched;
    }

    public void incrementSearchCount(final Long searchCount) {
        this.searchCount += searchCount;
    }
}
