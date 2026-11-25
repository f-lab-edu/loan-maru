package me.ngyu.loanmaru.application.loanaccount.api;

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
}
