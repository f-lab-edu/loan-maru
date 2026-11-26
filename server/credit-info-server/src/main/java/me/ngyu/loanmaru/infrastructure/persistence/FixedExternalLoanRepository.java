package me.ngyu.loanmaru.infrastructure.persistence;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoan;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoanRepository;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoanSnapshot;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import me.ngyu.loanmaru.common.exception.ErrorCode;
import org.springframework.stereotype.Repository;

/**
 * 신용정보원 연계 대신 테스트 고객별로 고정된 타 기관 대출을 돌려주는 임시 저장소.
 * 기준 시각은 직전 정각이다. 목록에 없는 고객은 대출이 없고, CI-CREDIT-ERROR는 조회 실패로 응답한다.
 */
@Repository
public class FixedExternalLoanRepository implements ExternalLoanRepository {

  @Override
  public ExternalLoanSnapshot findSnapshotByCustCi(String custCi) {
    LocalDateTime baseDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
    List<ExternalLoan> loans = switch (custCi) {
      case "CI-8f3a2b1c9d4e" ->
              List.of(new ExternalLoan("B004", "CREDIT", 15_000_000L, 4_200_000L));
      case "CI-CREDIT-ERROR" -> throw new ApplicationException(ErrorCode.INQUIRY_FAILED);
      default -> List.of();
    };
    return new ExternalLoanSnapshot(baseDateTime, loans);
  }
}
