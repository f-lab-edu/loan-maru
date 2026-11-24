package me.ngyu.loanmaru.application.loanaccount.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccount;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanAccountService {

  private final LoanAccountRepository loanAccountRepository;

  public List<LoanAccount> findLoanAccounts(String custCi) {
    return loanAccountRepository.findAllByCustCi(custCi);
  }
}
