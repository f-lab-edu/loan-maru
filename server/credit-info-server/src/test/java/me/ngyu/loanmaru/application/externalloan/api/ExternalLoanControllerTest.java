package me.ngyu.loanmaru.application.externalloan.api;

import static org.hamcrest.Matchers.matchesPattern;
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
}
