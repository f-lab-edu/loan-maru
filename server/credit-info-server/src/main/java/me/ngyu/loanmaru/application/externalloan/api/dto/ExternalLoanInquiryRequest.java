package me.ngyu.loanmaru.application.externalloan.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ExternalLoanInquiryRequest(@NotBlank String custCi) {
}
