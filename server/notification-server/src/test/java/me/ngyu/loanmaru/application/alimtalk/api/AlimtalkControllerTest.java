package me.ngyu.loanmaru.application.alimtalk.api;

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
class AlimtalkControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("알림톡 발송을 접수하면 발송 식별자를 돌려준다.")
  void send() throws Exception {
    mockMvc.perform(post("/notifications/alimtalk")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "screeningId": "LS-20261119-000123",
                              "message": "고객님의 대출 심사 결과는 감액 승인입니다.",
                              "buttons": [{"name": "심사 결과 확인", "url": "https://example.com"}]
                            }
                            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.messageId").value(matchesPattern("MSG-\\d{8}-\\d{4}")));
  }

  @Test
  @DisplayName("버튼 목록에 null이 있으면 400으로 응답한다.")
  void rejectNullButton() throws Exception {
    mockMvc.perform(post("/notifications/alimtalk")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"screeningId": "S-1", "message": "안내", "buttons": [null]}
                            """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
  }

  @Test
  @DisplayName("버튼을 생략해도 알림톡 발송을 접수한다.")
  void sendWithoutButtons() throws Exception {
    mockMvc.perform(post("/notifications/alimtalk")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"screeningId": "S-1", "message": "안내"}
                            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.messageId").isString());
  }
}
