package me.ngyu.loanmaru.application.loanaccount.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoanAccountListRequest(@NotBlank String custCi) {
}
