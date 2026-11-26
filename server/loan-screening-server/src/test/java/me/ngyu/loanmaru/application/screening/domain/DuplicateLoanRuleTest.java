package me.ngyu.loanmaru.application.screening.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import me.ngyu.loanmaru.application.product.domain.ProductCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DuplicateLoanRuleTest {

  private static final int MAX_COUNT = 1;

  @ParameterizedTest(name = "{0}")
  @MethodSource("cases")
  @DisplayName("신청 상품과 같은 유형의 정상 계좌가 기준 건수보다 적을 때만 통과한다.")
  void passes(String description, List<LoanAccount> accounts, boolean expected) {
    assertThat(DuplicateLoanRule.passes(accounts, ProductCode.L01, MAX_COUNT)).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
            Arguments.of("계좌가 없으면 통과한다", List.of(), true),
            Arguments.of("같은 유형 정상 계좌가 1건이면 거절한다",
                    List.of(new LoanAccount(ProductCode.L01, "01")), false),
            Arguments.of("같은 유형이라도 완제·해지 계좌는 세지 않아 통과한다",
                    List.of(new LoanAccount(ProductCode.L01, "02"),
                            new LoanAccount(ProductCode.L01, "09")), true),
            Arguments.of("다른 유형의 정상 계좌는 세지 않아 통과한다",
                    List.of(new LoanAccount(ProductCode.L03, "01")), true)
    );
  }
}
