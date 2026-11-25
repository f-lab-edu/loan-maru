package me.ngyu.loanmaru.application.product.domain;

import java.util.List;

/**
 * 한 기준값 버전에 묶인 상품 목록과 공통 기준값.
 */
public record LoanProductCatalog(
        String thresholdVersion,
        List<LoanProduct> products,
        ScreeningThresholds thresholds
) {
}
