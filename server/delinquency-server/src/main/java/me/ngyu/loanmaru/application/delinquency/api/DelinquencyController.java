package me.ngyu.loanmaru.application.delinquency.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.delinquency.api.dto.DelinquencyListResponse;
import me.ngyu.loanmaru.application.delinquency.service.DelinquencyService;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import me.ngyu.loanmaru.common.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DelinquencyController {

  private final DelinquencyService delinquencyService;

  @GetMapping("/delinquencies")
  public DelinquencyListResponse findDelinquencies(@RequestParam("acctNo") List<String> acctNos) {
    if (acctNos.isEmpty() || acctNos.stream().anyMatch(String::isBlank)) {
      throw new ApplicationException(ErrorCode.VALIDATION_ERROR);
    }
    return DelinquencyListResponse.from(delinquencyService.findDelinquencies(acctNos));
  }
}
