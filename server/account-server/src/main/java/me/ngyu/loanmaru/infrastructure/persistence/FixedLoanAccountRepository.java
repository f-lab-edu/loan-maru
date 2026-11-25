package me.ngyu.loanmaru.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccount;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccountRepository;
import org.springframework.stereotype.Repository;

/**
 * 계정계 원장 대신 고정된 여신 계좌를 돌려주는 임시 저장소.
 */
@Repository
public class FixedLoanAccountRepository implements LoanAccountRepository {

  @Override
  public List<LoanAccount> findAllByCustCi(String custCi) {
    return List.of(new LoanAccount(
            "110012345678",
            custCi,
            "L01",
            "01",
            30_000_000L,
            24_000_000L,
            new BigDecimal("6.2"),
            7_488_000L,
            LocalDate.of(2025, 3, 1),
            LocalDate.of(2030, 3, 1),
            "A"
    ));
  }
}
