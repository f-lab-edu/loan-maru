package me.ngyu.loanmaru.application.loanaccount.domain;

import java.util.List;

public interface LoanAccountRepository {

  List<LoanAccount> findAllByCustCi(String custCi);
}
