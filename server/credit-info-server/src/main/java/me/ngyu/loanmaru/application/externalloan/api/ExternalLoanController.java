package me.ngyu.loanmaru.application.externalloan.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ngyu.loanmaru.application.externalloan.api.dto.ExternalLoanInquiryRequest;
import me.ngyu.loanmaru.application.externalloan.api.dto.ExternalLoanInquiryResponse;
import me.ngyu.loanmaru.application.externalloan.service.ExternalLoanService;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExternalLoanController {

  private final ExternalLoanService externalLoanService;

  @PostMapping("/credit/loan/inquiry")
  public ExternalLoanInquiryResponse inquire(
          @Valid @RequestBody ExternalLoanInquiryRequest request) {
    return ExternalLoanInquiryResponse.success(externalLoanService.inquire(request.custCi()));
  }

  /**
   * 대외계 명세는 HTTP 상태가 아니라 status로 실패를 알리므로 실패 응답을 200으로 돌려준다.
   */
  @ExceptionHandler(ApplicationException.class)
  public ExternalLoanInquiryResponse handleFailure(ApplicationException exception) {
    log.error("타 기관 대출 조회에 실패했습니다. code={}", exception.getErrorCode().name(), exception);
    return ExternalLoanInquiryResponse.fail();
  }
}
