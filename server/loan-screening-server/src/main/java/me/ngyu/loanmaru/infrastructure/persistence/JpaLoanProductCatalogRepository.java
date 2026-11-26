package me.ngyu.loanmaru.infrastructure.persistence;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalog;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalogRepository;
import me.ngyu.loanmaru.application.product.domain.ThresholdVersion;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import me.ngyu.loanmaru.common.exception.ErrorCode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JpaLoanProductCatalogRepository implements LoanProductCatalogRepository {

  private final ThresholdVersionJpaRepository thresholdVersionJpaRepository;

  @Override
  @Transactional(readOnly = true)
  public LoanProductCatalog findCurrent() {
    ThresholdVersion version = thresholdVersionJpaRepository
            .findFirstByEffectiveFromLessThanEqualOrderByEffectiveFromDesc(Instant.now())
            .orElseThrow(() -> new ApplicationException(ErrorCode.THRESHOLD_VERSION_NOT_FOUND));
    // 상품 목록은 지연 로딩이라 트랜잭션 안에서 복사해 둔다.
    return new LoanProductCatalog(
            version.getVersionKey(),
            List.copyOf(version.getProducts()),
            version.getThresholds()
    );
  }
}
