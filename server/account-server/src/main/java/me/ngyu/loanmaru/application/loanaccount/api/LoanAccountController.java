package me.ngyu.loanmaru.application.loanaccount.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.loanaccount.api.dto.LoanAccountListRequest;
import me.ngyu.loanmaru.application.loanaccount.api.dto.LoanAccountListResponse;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccount;
import me.ngyu.loanmaru.application.loanaccount.service.LoanAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoanAccountController {

  private final LoanAccountService loanAccountService;

  @PostMapping("/if/loan/inqLoanAcctList")
  public LoanAccountListResponse findLoanAccounts(
          @Valid @RequestBody LoanAccountListRequest request) {
    List<LoanAccount> accounts = loanAccountService.findLoanAccounts(request.custCi());
    return LoanAccountListResponse.success(accounts);
  }
}
