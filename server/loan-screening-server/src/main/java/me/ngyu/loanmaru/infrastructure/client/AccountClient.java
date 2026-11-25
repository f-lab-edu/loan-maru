package me.ngyu.loanmaru.infrastructure.client;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import me.ngyu.loanmaru.application.product.domain.ProductCode;
import me.ngyu.loanmaru.application.screening.domain.LoanAccount;
import me.ngyu.loanmaru.application.screening.domain.LoanAccounts;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import me.ngyu.loanmaru.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * 계정계의 여신 계좌 목록 조회 전문을 호출한다.
 */
@Component
public class AccountClient implements LoanAccounts {

  private static final String LOAN_ACCOUNT_LIST_PATH = "/if/loan/inqLoanAcctList";
  private static final String SUCCESS_CODE = "0000";

  private final RestClient restClient;

  @Autowired
  public AccountClient(@Value("${client.account.url}") String baseUrl) {
    this(RestClient.builder(), baseUrl);
  }

  AccountClient(RestClient.Builder builder, String baseUrl) {
    this.restClient = builder.baseUrl(baseUrl).build();
  }

  @Override
  public List<LoanAccount> findByCustCi(String custCi) {
    LoanAccountListResponse response = requestLoanAccounts(custCi);
    if (response == null
            || !SUCCESS_CODE.equals(response.resultCode())
            || response.acctList() == null) {
      throw new ApplicationException(ErrorCode.EXTERNAL_SYSTEM_UNAVAILABLE);
    }
    return response.acctList().stream()
            .flatMap(account -> toLoanAccount(account).stream())
            .toList();
  }

  private LoanAccountListResponse requestLoanAccounts(String custCi) {
    try {
      return restClient.post()
              .uri(LOAN_ACCOUNT_LIST_PATH)
              .body(new LoanAccountListRequest(custCi))
              .retrieve()
              .body(LoanAccountListResponse.class);
    } catch (RestClientException exception) {
      throw new ApplicationException(ErrorCode.EXTERNAL_SYSTEM_UNAVAILABLE, exception);
    }
  }

  // 우리 상품에 없는 유형의 계좌는 중복 비교 대상이 아니므로 건너뛴다.
  private static Optional<LoanAccount> toLoanAccount(Account account) {
    return Arrays.stream(ProductCode.values())
            .filter(code -> code.name().equals(account.prdtTpCd()))
            .findFirst()
            .map(code -> new LoanAccount(code, account.acctStCd()));
  }

  private record LoanAccountListRequest(String custCi) {
  }

  private record LoanAccountListResponse(
          String resultCode,
          String resultMsg,
          List<Account> acctList
  ) {
  }

  private record Account(String acctNo, String prdtTpCd, String acctStCd) {
  }
}
