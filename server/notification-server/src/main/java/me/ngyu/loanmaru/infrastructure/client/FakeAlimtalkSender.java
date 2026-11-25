package me.ngyu.loanmaru.infrastructure.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import me.ngyu.loanmaru.application.alimtalk.domain.Alimtalk;
import me.ngyu.loanmaru.application.alimtalk.domain.AlimtalkSender;
import org.springframework.stereotype.Component;

/**
 * 실제 발송 없이 접수만 기록하고 발송 식별자를 만드는 임시 발송기.
 */
@Slf4j
@Component
public class FakeAlimtalkSender implements AlimtalkSender {

  private final AtomicLong sequence = new AtomicLong();

  @Override
  public String send(Alimtalk alimtalk) {
    String messageId = "MSG-%s-%04d".formatted(
            LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE),
            sequence.incrementAndGet()
    );
    log.info("알림톡 발송을 접수했습니다. screeningId={}, messageId={}",
            alimtalk.screeningId(), messageId);
    return messageId;
  }
}
