package me.ngyu.loanmaru.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import me.ngyu.loanmaru.application.delinquency.domain.Delinquency;
import me.ngyu.loanmaru.application.delinquency.domain.DelinquencyRepository;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import me.ngyu.loanmaru.common.exception.ErrorCode;
import org.springframework.stereotype.Repository;

/**
 * 여신사후관리 원장 대신 테스트 계좌별로 고정된 연체 이력을 돌려주는 임시 저장소.
 * 목록에 없는 계좌는 연체가 없고, 110000000005는 장애를 흉내 내 503으로 응답한다.
 */
@Repository
public class FixedDelinquencyRepository implements DelinquencyRepository {

  @Override
  public List<Delinquency> findAllByAccountNo(String accountNo) {
    return switch (accountNo) {
      case "110012345678" -> List.of(new Delinquency(
              "D202606001",
              LocalDate.of(2026, 6, 10),
              LocalDate.of(2026, 7, 15),
              35
      ));
      case "110000000004" -> {
        LocalDate today = LocalDate.now();
        yield List.of(new Delinquency(
                "D000000004",
                today.minusDays(65),
                today.minusDays(30),
                35
        ));
      }
      case "110000000005" -> throw new ApplicationException(ErrorCode.SERVICE_UNAVAILABLE);
      default -> List.of();
    };
  }
}
