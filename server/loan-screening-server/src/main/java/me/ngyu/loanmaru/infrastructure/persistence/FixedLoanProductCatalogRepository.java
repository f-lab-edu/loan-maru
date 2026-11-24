package me.ngyu.loanmaru.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.List;
import me.ngyu.loanmaru.application.product.domain.Eligibility;
import me.ngyu.loanmaru.application.product.domain.LoanProduct;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalog;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalogRepository;
import me.ngyu.loanmaru.application.product.domain.ProductCode;
import me.ngyu.loanmaru.application.product.domain.ScreeningRule;
import me.ngyu.loanmaru.application.product.domain.ScreeningThresholds;
import org.springframework.stereotype.Repository;

/**
 * 데이터 모델이 정해지기 전까지 API 문서의 예시 값을 돌려주는 임시 저장소.
 */
@Repository
public class FixedLoanProductCatalogRepository implements LoanProductCatalogRepository {

  @Override
  public LoanProductCatalog findCurrent() {
    return new LoanProductCatalog(
            "2026-11-01.v3",
            List.of(
                    new LoanProduct(
                            ProductCode.L01,
                            "신용대출",
                            100_000_000L,
                            new BigDecimal("6.0"),
                            true,
                            List.of(ScreeningRule.values()),
                            new Eligibility(null, 24_000_000L, true)
                    ),
                    new LoanProduct(
                            ProductCode.L03,
                            "비상금대출",
                            3_000_000L,
                            new BigDecimal("9.0"),
                            false,
                            List.of(
                                    ScreeningRule.ELIGIBILITY,
                                    ScreeningRule.DUPLICATE_LOAN,
                                    ScreeningRule.DELINQUENCY,
                                    ScreeningRule.PRODUCT_MAX_LIMIT
                            ),
                            new Eligibility(19, null, false)
                    )
            ),
            new ScreeningThresholds(1, 6, 30, new BigDecimal("40.0"), 100_000L, 5)
    );
  }
}
