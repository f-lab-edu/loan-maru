package me.ngyu.loanmaru.application.externalloan.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 기준 시각(직전 정각)에 집계된 타 금융기관 대출 목록.
 */
public record ExternalLoanSnapshot(LocalDateTime baseDateTime, List<ExternalLoan> loans) {
}
