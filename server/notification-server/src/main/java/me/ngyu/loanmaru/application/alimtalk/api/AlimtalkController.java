package me.ngyu.loanmaru.application.alimtalk.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.alimtalk.api.dto.AlimtalkSendRequest;
import me.ngyu.loanmaru.application.alimtalk.api.dto.AlimtalkSendResponse;
import me.ngyu.loanmaru.application.alimtalk.service.AlimtalkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlimtalkController {

  private final AlimtalkService alimtalkService;

  @PostMapping("/notifications/alimtalk")
  public AlimtalkSendResponse send(@Valid @RequestBody AlimtalkSendRequest request) {
    return new AlimtalkSendResponse(alimtalkService.send(request.toAlimtalk()));
  }
}
