package me.ngyu.loanmaru.application.alimtalk.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import me.ngyu.loanmaru.application.alimtalk.domain.Alimtalk;
import me.ngyu.loanmaru.application.alimtalk.domain.AlimtalkButton;

public record AlimtalkSendRequest(
        @NotBlank String screeningId,
        @NotBlank String message,
        List<@NotNull @Valid Button> buttons
) {

  public Alimtalk toAlimtalk() {
    List<AlimtalkButton> alimtalkButtons = buttons == null
            ? List.of()
            : buttons.stream().map(Button::toAlimtalkButton).toList();
    return new Alimtalk(screeningId, message, alimtalkButtons);
  }

  public record Button(@NotBlank String name, @NotBlank String url) {

    AlimtalkButton toAlimtalkButton() {
      return new AlimtalkButton(name, url);
    }
  }
}
