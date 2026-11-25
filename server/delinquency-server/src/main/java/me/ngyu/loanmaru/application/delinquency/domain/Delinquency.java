package me.ngyu.loanmaru.application.delinquency.domain;

import java.time.LocalDate;

/**
 * 계좌의 연체 1건. 진행 중이면 endDate가 null이다.
 */
public record Delinquency(
        String delinquencyId,
        LocalDate startDate,
        LocalDate endDate,
        int overdueDays
) {
}
