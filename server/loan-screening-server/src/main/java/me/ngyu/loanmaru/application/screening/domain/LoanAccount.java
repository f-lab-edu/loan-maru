package me.ngyu.loanmaru.application.screening.domain;

import me.ngyu.loanmaru.application.product.domain.ProductCode;

/**
 * 심사에 쓰는 자사 여신 계좌. 판정에 필요한 값만 둔다.
 */
public record LoanAccount(ProductCode productCode, String statusCode) {
}
