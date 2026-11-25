package me.ngyu.loanmaru.infrastructure.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;
import me.ngyu.loanmaru.application.product.domain.ProductCode;
import me.ngyu.loanmaru.application.screening.domain.LoanAccount;
import me.ngyu.loanmaru.common.exception.ApplicationException;
import me.ngyu.loanmaru.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class AccountClientTest {

  private static final String BASE_URL = "http://account-server";
  private static final String CUST_CI = "CI-8f3a2b1c9d4e";

  private MockRestServiceServer server;
  private AccountClient accountClient;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder();
    server = MockRestServiceServer.bindTo(builder).build();
    accountClient = new AccountClient(builder, BASE_URL);
  }

  @Test
  @DisplayName("계정계 전문으로 계좌를 조회하고 모르는 상품 유형의 계좌는 건너뛴다.")
  void findByCustCi() {
    server.expect(requestTo(BASE_URL + "/if/loan/inqLoanAcctList"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("{\"custCi\":\"" + CUST_CI + "\"}", JsonCompareMode.STRICT))
            .andRespond(withSuccess("""
                    {
                      "resultCode": "0000",
                      "resultMsg": "정상",
                      "acctList": [
                        {"acctNo": "110012345678", "prdtTpCd": "L01", "acctStCd": "01", "loanAmt": 30000000},
                        {"acctNo": "110000000002", "prdtTpCd": "L03", "acctStCd": "02"},
                        {"acctNo": "110000000009", "prdtTpCd": "X99", "acctStCd": "01"}
                      ]
                    }
                    """, MediaType.APPLICATION_JSON));

    List<LoanAccount> accounts = accountClient.findByCustCi(CUST_CI);

    assertThat(accounts).containsExactly(
            new LoanAccount(ProductCode.L01, "01"),
            new LoanAccount(ProductCode.L03, "02"));
    server.verify();
  }

  @Test
  @DisplayName("계정계가 실패 코드를 주면 외부 시스템 장애 예외를 던진다.")
  void failureResultCode() {
    server.expect(requestTo(BASE_URL + "/if/loan/inqLoanAcctList"))
            .andRespond(withSuccess("""
                    {"resultCode": "9999", "resultMsg": "시스템 오류", "acctList": null}
                    """, MediaType.APPLICATION_JSON));

    assertThatThrownBy(() -> accountClient.findByCustCi(CUST_CI))
            .isInstanceOf(ApplicationException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.EXTERNAL_SYSTEM_UNAVAILABLE);
  }

  @Test
  @DisplayName("계정계 통신이 실패하면 외부 시스템 장애 예외를 던진다.")
  void communicationFailure() {
    server.expect(requestTo(BASE_URL + "/if/loan/inqLoanAcctList"))
            .andRespond(withServerError());

    assertThatThrownBy(() -> accountClient.findByCustCi(CUST_CI))
            .isInstanceOf(ApplicationException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.EXTERNAL_SYSTEM_UNAVAILABLE);
  }
}
