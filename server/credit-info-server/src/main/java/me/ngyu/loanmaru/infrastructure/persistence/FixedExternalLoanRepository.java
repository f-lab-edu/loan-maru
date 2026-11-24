package me.ngyu.loanmaru.infrastructure.persistence;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoan;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoanRepository;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoanSnapshot;
import org.springframework.stereotype.Repository;

/**
 * 신용정보원 연계 대신 고정된 타 기관 대출을 돌려주는 임시 저장소. 기준 시각은 직전 정각이다.
 */
@Repository
public class FixedExternalLoanRepository implements ExternalLoanRepository {

  @Override
  public ExternalLoanSnapshot findSnapshotByCustCi(String custCi) {
    LocalDateTime baseDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
    return new ExternalLoanSnapshot(
            baseDateTime,
            List.of(new ExternalLoan("B004", "CREDIT", 15_000_000L, 4_200_000L))
    );
  }
}
