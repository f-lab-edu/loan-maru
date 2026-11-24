package me.ngyu.loanmaru.application.loanaccount.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanAccount(
        String accountNo,
        String custCi,
        String productTypeCode,
        String accountStatusCode,
        long loanAmount,
        long loanBalance,
        BigDecimal interestRate,
        long annualRepaymentAmount,
        LocalDate loanStartDate,
        LocalDate maturityDate,
        String channelCode
) {
}
