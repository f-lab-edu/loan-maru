package me.ngyu.loanmaru.application.loanaccount.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ngyu.loanmaru.application.loanaccount.api.dto.LoanAccountListRequest;
import me.ngyu.loanmaru.application.loanaccount.api.dto.LoanAccountListResponse;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccount;
import me.ngyu.loanmaru.application.loanaccount.service.LoanAccountService;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoanAccountController {

  private final LoanAccountService loanAccountService;

  @PostMapping("/if/loan/inqLoanAcctList")
  public LoanAccountListResponse findLoanAccounts(
          @Valid @RequestBody LoanAccountListRequest request) {
    List<LoanAccount> accounts = loanAccountService.findLoanAccounts(request.custCi());
    return LoanAccountListResponse.success(accounts);
  }

  /**
   * 계정계 명세는 HTTP 상태가 아니라 resultCode로 실패를 알리므로 실패 전문을 200으로 돌려준다.
   */
  @ExceptionHandler(ApplicationException.class)
  public LoanAccountListResponse handleFailure(ApplicationException exception) {
    log.error("여신 계좌 조회에 실패했습니다. code={}", exception.getErrorCode().name(), exception);
    return LoanAccountListResponse.failure();
  }
}
