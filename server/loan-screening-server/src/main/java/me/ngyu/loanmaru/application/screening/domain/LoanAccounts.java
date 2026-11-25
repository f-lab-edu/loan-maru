package me.ngyu.loanmaru.application.screening.domain;

import java.util.List;

/**
 * 계정계에서 고객의 자사 여신 계좌를 조회한다.
 */
public interface LoanAccounts {

  List<LoanAccount> findByCustCi(String custCi);
}
