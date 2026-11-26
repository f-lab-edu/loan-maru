package me.ngyu.loanmaru.application.externalloan.api;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class ExternalLoanControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("차주 CI로 조회하면 직전 정각 기준의 타 기관 대출 목록을 돌려준다.")
  void inquire() throws Exception {
    mockMvc.perform(post("/credit/loan/inquiry")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"custCi": "CI-8f3a2b1c9d4e"}
                            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.baseDateTime").value(matchesPattern(".*T\\d{2}:00:00")))
            .andExpect(jsonPath("$.loanList[0].institutionCd").value("B004"));
  }

  @ParameterizedTest
  @ValueSource(strings = {
          "CI-DUPLICATE",
          "CI-CLOSED",
          "CI-DELINQUENT",
          "CI-ACCOUNT-ERROR",
          "CI-DELINQUENCY-ERROR",
          "CI-UNKNOWN"
  })
  @DisplayName("대외계 오류 고객이 아닌 테스트 고객과 목록에 없는 차주는 타 기관 대출이 없다.")
  void inquireNoLoans(String custCi) throws Exception {
    inquire(custCi)
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.baseDateTime").value(matchesPattern(".*T\\d{2}:00:00")))
            .andExpect(jsonPath("$.loanList").isEmpty());
  }

  @Test
  @DisplayName("대외계 오류 테스트 고객은 기준 시각과 대출 목록이 null인 실패 응답을 돌려준다.")
  void inquireFail() throws Exception {
    inquire("CI-CREDIT-ERROR")
            .andExpect(jsonPath("$.status").value("FAIL"))
            .andExpect(jsonPath("$.baseDateTime").value(nullValue()))
            .andExpect(jsonPath("$.loanList").value(nullValue()));
  }

  private ResultActions inquire(String custCi) throws Exception {
    return mockMvc.perform(post("/credit/loan/inquiry")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"custCi\": \"" + custCi + "\"}"))
            .andExpect(status().isOk());
  }
}
