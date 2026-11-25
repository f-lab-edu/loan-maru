package me.ngyu.loanmaru.application.product.domain;

/**
 * 상품 신청 자격. 기준이 없는 항목은 null이다.
 */
public record Eligibility(Integer minAge, Long minAnnualIncome, boolean employmentRequired) {
}
