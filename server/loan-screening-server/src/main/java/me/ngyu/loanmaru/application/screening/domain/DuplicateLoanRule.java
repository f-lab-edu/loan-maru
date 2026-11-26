package me.ngyu.loanmaru.application.screening.domain;

import java.util.List;
import me.ngyu.loanmaru.application.product.domain.ProductCode;

/**
 * 같은 상품 유형의 정상 계좌가 기준 건수보다 적을 때만 통과시킨다.
 */
public final class DuplicateLoanRule {

  private static final String ACTIVE_STATUS = "01";

  private DuplicateLoanRule() {
  }

  public static boolean passes(List<LoanAccount> accounts, ProductCode product, int maxCount) {
    long activeCount = accounts.stream()
            .filter(account -> account.productCode() == product)
            .filter(account -> ACTIVE_STATUS.equals(account.statusCode()))
            .count();
    return activeCount < maxCount;
  }
}
