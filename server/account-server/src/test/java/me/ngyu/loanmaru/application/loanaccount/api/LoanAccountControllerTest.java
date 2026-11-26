package me.ngyu.loanmaru.application.loanaccount.api;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class LoanAccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("차주 CI로 조회하면 계정계 전문 형식으로 여신 계좌 목록을 돌려준다.")
  void findLoanAccounts() throws Exception {
    mockMvc.perform(post("/if/loan/inqLoanAcctList")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"custCi": "CI-8f3a2b1c9d4e"}
                            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultCode").value("0000"))
            .andExpect(jsonPath("$.acctList[0].custCi").value("CI-8f3a2b1c9d4e"))
            .andExpect(jsonPath("$.acctList[0].loanStrDt").value("20250301"));
  }

  @Test
  @DisplayName("중복 대출 테스트 고객은 정상 상태의 L01 계좌 1건을 돌려준다.")
  void duplicateCustomer() throws Exception {
    findByCustCi("CI-DUPLICATE")
            .andExpect(jsonPath("$.resultCode").value("0000"))
            .andExpect(jsonPath("$.acctList.length()").value(1))
            .andExpect(jsonPath("$.acctList[0].acctNo").value("110000000001"))
            .andExpect(jsonPath("$.acctList[0].prdtTpCd").value("L01"))
            .andExpect(jsonPath("$.acctList[0].acctStCd").value("01"));
  }

  @Test
  @DisplayName("완제·해지 테스트 고객은 완제와 해지 상태의 L01 계좌를 돌려준다.")
  void closedCustomer() throws Exception {
    findByCustCi("CI-CLOSED")
            .andExpect(jsonPath("$.acctList.length()").value(2))
            .andExpect(jsonPath("$.acctList[0].acctNo").value("110000000002"))
            .andExpect(jsonPath("$.acctList[0].acctStCd").value("02"))
            .andExpect(jsonPath("$.acctList[1].acctNo").value("110000000003"))
            .andExpect(jsonPath("$.acctList[1].acctStCd").value("09"));
  }

  @Test
  @DisplayName("연체 테스트 고객은 정상 상태의 L03 계좌를 돌려준다.")
  void delinquentCustomer() throws Exception {
    findByCustCi("CI-DELINQUENT")
            .andExpect(jsonPath("$.acctList.length()").value(1))
            .andExpect(jsonPath("$.acctList[0].acctNo").value("110000000004"))
            .andExpect(jsonPath("$.acctList[0].prdtTpCd").value("L03"))
            .andExpect(jsonPath("$.acctList[0].acctStCd").value("01"));
  }

  @Test
  @DisplayName("여신사후관리 장애 테스트 고객은 장애 계좌번호의 L03 계좌를 돌려준다.")
  void delinquencyErrorCustomer() throws Exception {
    findByCustCi("CI-DELINQUENCY-ERROR")
            .andExpect(jsonPath("$.acctList.length()").value(1))
            .andExpect(jsonPath("$.acctList[0].acctNo").value("110000000005"))
            .andExpect(jsonPath("$.acctList[0].prdtTpCd").value("L03"));
  }

  @Test
  @DisplayName("계정계 오류 테스트 고객은 resultCode 9999와 null 계좌 목록을 돌려준다.")
  void accountErrorCustomer() throws Exception {
    findByCustCi("CI-ACCOUNT-ERROR")
            .andExpect(jsonPath("$.resultCode").value("9999"))
            .andExpect(jsonPath("$.resultMsg").value("시스템 오류"))
            .andExpect(jsonPath("$.acctList").value(nullValue()));
  }

  @Test
  @DisplayName("대외계 오류 테스트 고객은 계좌가 없다.")
  void creditErrorCustomer() throws Exception {
    findByCustCi("CI-CREDIT-ERROR")
            .andExpect(jsonPath("$.resultCode").value("0000"))
            .andExpect(jsonPath("$.acctList").isEmpty());
  }

  @Test
  @DisplayName("테스트 고객 목록에 없는 차주는 빈 계좌 목록을 돌려준다.")
  void unknownCustomer() throws Exception {
    findByCustCi("CI-UNKNOWN")
            .andExpect(jsonPath("$.resultCode").value("0000"))
            .andExpect(jsonPath("$.acctList").isEmpty());
  }

  @Test
  @DisplayName("차주 CI가 비어 있으면 400으로 응답한다.")
  void rejectBlankCustCi() throws Exception {
    mockMvc.perform(post("/if/loan/inqLoanAcctList")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"custCi": ""}
                            """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
  }

  private ResultActions findByCustCi(String custCi) throws Exception {
    return mockMvc.perform(post("/if/loan/inqLoanAcctList")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"custCi\": \"" + custCi + "\"}"))
            .andExpect(status().isOk());
  }
}
