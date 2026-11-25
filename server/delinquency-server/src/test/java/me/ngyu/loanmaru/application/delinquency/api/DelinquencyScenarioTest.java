package me.ngyu.loanmaru.application.delinquency.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DelinquencyScenarioTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("중복 대출 테스트 계좌는 연체가 없다.")
  void duplicateAccount() throws Exception {
    mockMvc.perform(get("/delinquencies").queryParam("acctNo", "110000000001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.results[0].acctNo").value("110000000001"))
            .andExpect(jsonPath("$.results[0].delinquencies").isEmpty());
  }

  @Test
  @DisplayName("완제·해지 테스트 계좌는 연체가 없다.")
  void closedAccounts() throws Exception {
    mockMvc.perform(get("/delinquencies").queryParam("acctNo", "110000000002", "110000000003"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.results[0].delinquencies").isEmpty())
            .andExpect(jsonPath("$.results[1].delinquencies").isEmpty());
  }

  @Test
  @DisplayName("연체 테스트 계좌는 오늘 기준 30일 전에 해소된 35일 연체 1건을 돌려준다.")
  void delinquentAccount() throws Exception {
    LocalDate today = LocalDate.now();
    mockMvc.perform(get("/delinquencies").queryParam("acctNo", "110000000004"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.results[0].delinquencies.length()").value(1))
            .andExpect(jsonPath("$.results[0].delinquencies[0].startDate")
                    .value(today.minusDays(65).toString()))
            .andExpect(jsonPath("$.results[0].delinquencies[0].endDate")
                    .value(today.minusDays(30).toString()))
            .andExpect(jsonPath("$.results[0].delinquencies[0].overdueDays").value(35));
  }

  @Test
  @DisplayName("장애 테스트 계좌가 포함되면 다른 계좌와 함께 조회해도 503으로 응답한다.")
  void unavailableAccount() throws Exception {
    mockMvc.perform(get("/delinquencies").queryParam("acctNo", "110000000004", "110000000005"))
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.code").value("SERVICE_UNAVAILABLE"));
  }

  @Test
  @DisplayName("테스트 계좌 목록에 없는 계좌는 연체가 없다.")
  void unknownAccount() throws Exception {
    mockMvc.perform(get("/delinquencies").queryParam("acctNo", "999999999999"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.results[0].delinquencies").isEmpty());
  }
}
