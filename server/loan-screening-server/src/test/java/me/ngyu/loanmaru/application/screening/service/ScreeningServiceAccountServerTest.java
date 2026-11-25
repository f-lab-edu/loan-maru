package me.ngyu.loanmaru.application.screening.service;

import static org.assertj.core.api.Assertions.assertThat;

import me.ngyu.loanmaru.application.product.domain.ProductCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 실행 중인 account-server를 실제로 호출한다. ACCOUNT_SERVER_URL이 있을 때만 돈다.
 */
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "ACCOUNT_SERVER_URL", matches = ".+")
class ScreeningServiceAccountServerTest {

  @Autowired
  private ScreeningService screeningService;

  @Test
  @DisplayName("계정계에 같은 유형의 정상 계좌가 있으면 중복 대출로 거절한다.")
  void rejectsDuplicateLoan() {
    assertThat(screeningService.checkDuplicateLoan("CI-8f3a2b1c9d4e", ProductCode.L01)).isFalse();
  }
}
