package me.ngyu.loanmaru.application.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * 상품 신청 자격. 기준이 없는 항목은 null이다.
 */
@Embeddable
public record Eligibility(
        Integer minAge,
        Long minAnnualIncome,
        @Column(nullable = false) boolean employmentRequired
) {
}
