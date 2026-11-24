package me.ngyu.loanmaru.application.externalloan.api.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoan;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoanSnapshot;

public record ExternalLoanInquiryResponse(
        String status,
        String baseDateTime,
        List<Loan> loanList
) {

  private static final DateTimeFormatter DATE_TIME_FORMAT =
          DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  public static ExternalLoanInquiryResponse success(ExternalLoanSnapshot snapshot) {
    return new ExternalLoanInquiryResponse(
            "SUCCESS",
            snapshot.baseDateTime().format(DATE_TIME_FORMAT),
            snapshot.loans().stream().map(Loan::from).toList()
    );
  }

  public record Loan(
          String institutionCd,
          String loanTpCd,
          long balance,
          long annualRepaymentAmount
  ) {

    static Loan from(ExternalLoan loan) {
      return new Loan(
              loan.institutionCode(),
              loan.loanTypeCode(),
              loan.balance(),
              loan.annualRepaymentAmount()
      );
    }
  }
}
