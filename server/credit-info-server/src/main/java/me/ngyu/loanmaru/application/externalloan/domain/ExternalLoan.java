package me.ngyu.loanmaru.application.externalloan.domain;

public record ExternalLoan(
        String institutionCode,
        String loanTypeCode,
        long balance,
        long annualRepaymentAmount
) {
}
