package me.ngyu.loanmaru.application.alimtalk.service;

import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.alimtalk.domain.Alimtalk;
import me.ngyu.loanmaru.application.alimtalk.domain.AlimtalkSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlimtalkService {

  private final AlimtalkSender alimtalkSender;

  public String send(Alimtalk alimtalk) {
    return alimtalkSender.send(alimtalk);
  }
}
