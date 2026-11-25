package me.ngyu.loanmaru.application.product.domain;

import java.math.BigDecimal;

/**
 * 모든 상품에 공통으로 쓰는 심사 기준값.
 */
public record ScreeningThresholds(
        int duplicateLoanMaxCount,
        int delinquencyLookbackMonths,
        int delinquencyMinOverdueDays,
        BigDecimal dsrLimitRate,
        long reductionUnitAmount,
        int loanTermYears
) {
}
