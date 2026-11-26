package me.ngyu.loanmaru.application.product.api;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LoanProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("현재 기준값 버전의 상품 목록과 공통 기준값을 돌려준다.")
  void getLoanProducts() throws Exception {
    mockMvc.perform(get("/api/v1/loan-products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.thresholdVersion").value("2026-11-01.v3"))
            .andExpect(jsonPath("$.products[0].productCode").value("L01"))
            .andExpect(jsonPath("$.products[0].appliedRules").value(containsInAnyOrder(
                    "ELIGIBILITY", "DUPLICATE_LOAN", "DELINQUENCY", "PRODUCT_MAX_LIMIT", "DSR")))
            .andExpect(jsonPath("$.products[1].eligibility.minAge").value(19))
            .andExpect(jsonPath("$.thresholds.dsrLimitRate").value(40.0));
  }
}
