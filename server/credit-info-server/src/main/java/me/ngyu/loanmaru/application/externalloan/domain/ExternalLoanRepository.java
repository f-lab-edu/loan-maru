package me.ngyu.loanmaru.application.externalloan.domain;

public interface ExternalLoanRepository {

  ExternalLoanSnapshot findSnapshotByCustCi(String custCi);
}
