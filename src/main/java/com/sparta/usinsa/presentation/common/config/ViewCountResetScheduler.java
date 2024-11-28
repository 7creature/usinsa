package com.sparta.usinsa.presentation.common.config;

import com.sparta.usinsa.application.service.ProductServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewCountResetScheduler {

  private final ProductServiceV2 productServiceV2;
  
//  @Scheduled(cron = "*/10 * * * * *") // 10 초마다 실행 -> 테스트
  @Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 실행
  public void resetViewCounts() {
    productServiceV2.resetViewCounts();
  }
}