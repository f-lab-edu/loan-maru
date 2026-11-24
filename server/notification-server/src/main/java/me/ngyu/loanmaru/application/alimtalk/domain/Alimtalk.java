package me.ngyu.loanmaru.application.alimtalk.domain;

import java.util.List;

public record Alimtalk(String screeningId, String message, List<AlimtalkButton> buttons) {
}
