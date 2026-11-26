package me.ngyu.loanmaru.application.screening.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalogRepository;
import me.ngyu.loanmaru.application.product.domain.ProductCode;
import me.ngyu.loanmaru.application.screening.domain.DuplicateLoanRule;
import me.ngyu.loanmaru.application.screening.domain.LoanAccount;
import me.ngyu.loanmaru.application.screening.domain.LoanAccounts;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreeningService {

  private final LoanAccounts loanAccounts;
  private final LoanProductCatalogRepository loanProductCatalogRepository;

  public boolean checkDuplicateLoan(String custCi, ProductCode product) {
    int maxCount = loanProductCatalogRepository.findCurrent().thresholds().duplicateLoanMaxCount();
    List<LoanAccount> accounts = loanAccounts.findByCustCi(custCi);
    return DuplicateLoanRule.passes(accounts, product, maxCount);
  }
}
