package me.ngyu.loanmaru.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccount;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccountRepository;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import me.ngyu.loanmaru.common.exception.ErrorCode;
import org.springframework.stereotype.Repository;

/**
 * 계정계 원장 대신 테스트 고객별로 고정된 여신 계좌를 돌려주는 임시 저장소. 목록에 없는 고객은 계좌가 없다.
 */
@Repository
public class FixedLoanAccountRepository implements LoanAccountRepository {

  @Override
  public List<LoanAccount> findAllByCustCi(String custCi) {
    return switch (custCi) {
      case "CI-8f3a2b1c9d4e" -> List.of(new LoanAccount(
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
      case "CI-DUPLICATE" -> List.of(account("110000000001", custCi, "L01", "01"));
      case "CI-CLOSED" -> List.of(
              account("110000000002", custCi, "L01", "02"),
              account("110000000003", custCi, "L01", "09"));
      case "CI-DELINQUENT" -> List.of(account("110000000004", custCi, "L03", "01"));
      case "CI-DELINQUENCY-ERROR" -> List.of(account("110000000005", custCi, "L03", "01"));
      case "CI-ACCOUNT-ERROR" -> throw new ApplicationException(ErrorCode.SYSTEM_ERROR);
      default -> List.of();
    };
  }

  private static LoanAccount account(
          String accountNo, String custCi, String productTypeCode, String accountStatusCode) {
    return new LoanAccount(
            accountNo,
            custCi,
            productTypeCode,
            accountStatusCode,
            10_000_000L,
            8_000_000L,
            new BigDecimal("6.2"),
            2_496_000L,
            LocalDate.of(2025, 3, 1),
            LocalDate.of(2030, 3, 1),
            "A"
    );
  }
}
