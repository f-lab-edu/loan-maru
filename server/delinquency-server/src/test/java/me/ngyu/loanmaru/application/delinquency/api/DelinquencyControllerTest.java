package me.ngyu.loanmaru.application.delinquency.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class DelinquencyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("여러 계좌를 조회하면 계좌마다 연체 목록을 돌려주고, 연체가 없으면 빈 배열이다.")
  void findDelinquencies() throws Exception {
    mockMvc.perform(get("/delinquencies")
                    .queryParam("acctNo", "110012345678", "110012345679")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.results[0].delinquencies[0].startDate").value("2026-06-10"))
            .andExpect(jsonPath("$.results[0].delinquencies[0].overdueDays").value(35))
            .andExpect(jsonPath("$.results[1].acctNo").value("110012345679"))
            .andExpect(jsonPath("$.results[1].delinquencies").isEmpty());
  }

  @Test
  @DisplayName("계좌번호 없이 조회하면 400으로 응답한다.")
  void rejectMissingAccountNo() throws Exception {
    mockMvc.perform(get("/delinquencies"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
  }
}
