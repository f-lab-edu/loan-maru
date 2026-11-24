package me.ngyu.loanmaru.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import me.ngyu.loanmaru.application.delinquency.domain.Delinquency;
import me.ngyu.loanmaru.application.delinquency.domain.DelinquencyRepository;
import org.springframework.stereotype.Repository;

/**
 * 여신사후관리 원장 대신 고정된 연체 이력을 돌려주는 임시 저장소.
 * 계좌번호 110012345678만 해소된 연체 1건이 있고, 나머지 계좌는 연체가 없다.
 */
@Repository
public class FixedDelinquencyRepository implements DelinquencyRepository {

  private static final String DELINQUENT_ACCOUNT_NO = "110012345678";

  @Override
  public List<Delinquency> findAllByAccountNo(String accountNo) {
    if (!DELINQUENT_ACCOUNT_NO.equals(accountNo)) {
      return List.of();
    }
    return List.of(new Delinquency(
            "D202606001",
            LocalDate.of(2026, 6, 10),
            LocalDate.of(2026, 7, 15),
            35
    ));
  }
}
