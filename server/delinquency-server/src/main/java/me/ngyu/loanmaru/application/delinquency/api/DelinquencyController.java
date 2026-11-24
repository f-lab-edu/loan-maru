package me.ngyu.loanmaru.application.delinquency.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.delinquency.api.dto.DelinquencyListResponse;
import me.ngyu.loanmaru.application.delinquency.service.DelinquencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DelinquencyController {

  private final DelinquencyService delinquencyService;

  @GetMapping("/delinquencies")
  public DelinquencyListResponse findDelinquencies(@RequestParam("acctNo") List<String> acctNos) {
    return DelinquencyListResponse.from(delinquencyService.findDelinquencies(acctNos));
  }
}
