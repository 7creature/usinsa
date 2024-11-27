package com.sparta.usinsa.batch;

import com.sparta.usinsa.domain.entity.Keyword;
import com.sparta.usinsa.domain.repository.KeywordRepository;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeywordBatchService {

    private final StringRedisTemplate redisTemplate;
    private final KeywordRepository keywordRepository;

    //    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(cron = "*/10 * * * * *") // 일단 10초
    @Transactional
    public void saveKeywordsToDatabase() {
        final Set<String> keys = redisTemplate.keys("search:*");

        for (String key : keys) {
            final String searchWord = key.substring("search:".length());

            final Set<ZSetOperations.TypedTuple<String>> entries = redisTemplate.opsForZSet()
                    .rangeWithScores(key, 0, -1);

            final long totalSearchCount = entries.stream()
                    .mapToLong(e -> e.getScore().longValue())
                    .sum();

            Optional<Keyword> existingKeyword = keywordRepository.findByKeyword(searchWord);
            if (existingKeyword.isPresent()) {
                existingKeyword.get().incrementSearchCount(totalSearchCount);
            } else {
                final Keyword newKeyword = new Keyword(searchWord);
                newKeyword.incrementSearchCount(totalSearchCount);
                keywordRepository.save(newKeyword);
            }

            redisTemplate.delete(key);
        }
    }
}
