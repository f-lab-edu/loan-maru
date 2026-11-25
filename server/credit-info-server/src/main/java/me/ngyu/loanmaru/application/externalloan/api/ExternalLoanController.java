package me.ngyu.loanmaru.application.externalloan.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.externalloan.api.dto.ExternalLoanInquiryRequest;
import me.ngyu.loanmaru.application.externalloan.api.dto.ExternalLoanInquiryResponse;
import me.ngyu.loanmaru.application.externalloan.service.ExternalLoanService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalLoanController {

  private final ExternalLoanService externalLoanService;

  @PostMapping("/credit/loan/inquiry")
  public ExternalLoanInquiryResponse inquire(
          @Valid @RequestBody ExternalLoanInquiryRequest request) {
    return ExternalLoanInquiryResponse.success(externalLoanService.inquire(request.custCi()));
  }
}
