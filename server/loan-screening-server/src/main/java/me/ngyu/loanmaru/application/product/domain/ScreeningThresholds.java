package me.ngyu.loanmaru.application.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * 모든 상품에 공통으로 쓰는 심사 기준값.
 */
@Embeddable
public record ScreeningThresholds(
        @Column(nullable = false) int duplicateLoanMaxCount,
        @Column(nullable = false) int delinquencyLookbackMonths,
        @Column(nullable = false) int delinquencyMinOverdueDays,
        @Column(nullable = false, precision = 5, scale = 2) BigDecimal dsrLimitRate,
        @Column(nullable = false) long reductionUnitAmount,
        @Column(nullable = false) int loanTermYears
) {
}
