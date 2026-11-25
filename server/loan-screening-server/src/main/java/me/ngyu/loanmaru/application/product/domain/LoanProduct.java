package me.ngyu.loanmaru.application.product.domain;

import java.math.BigDecimal;
import java.util.List;

public record LoanProduct(
        ProductCode productCode,
        String productName,
        long maxLimitAmount,
        BigDecimal interestRate,
        boolean dsrApplied,
        List<ScreeningRule> appliedRules,
        Eligibility eligibility
) {
}
