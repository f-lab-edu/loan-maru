package me.ngyu.loanmaru.application.product.api.dto;

import java.math.BigDecimal;
import java.util.List;
import me.ngyu.loanmaru.application.product.domain.Eligibility;
import me.ngyu.loanmaru.application.product.domain.LoanProduct;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalog;
import me.ngyu.loanmaru.application.product.domain.ProductCode;
import me.ngyu.loanmaru.application.product.domain.ScreeningRule;
import me.ngyu.loanmaru.application.product.domain.ScreeningThresholds;

public record LoanProductCatalogResponse(
        String thresholdVersion,
        List<Product> products,
        Thresholds thresholds
) {

  public static LoanProductCatalogResponse from(LoanProductCatalog catalog) {
    return new LoanProductCatalogResponse(
            catalog.thresholdVersion(),
            catalog.products().stream().map(Product::from).toList(),
            Thresholds.from(catalog.thresholds())
    );
  }

  public record Product(
          ProductCode productCode,
          String productName,
          long maxLimitAmount,
          BigDecimal interestRate,
          boolean dsrApplied,
          List<ScreeningRule> appliedRules,
          EligibilityResponse eligibility
  ) {

    static Product from(LoanProduct product) {
      return new Product(
              product.getProductCode(),
              product.getProductName(),
              product.getMaxLimitAmount(),
              product.getInterestRate(),
              product.isDsrApplied(),
              product.getAppliedRules(),
              EligibilityResponse.from(product.getEligibility())
      );
    }
  }

  public record EligibilityResponse(
          Integer minAge,
          Long minAnnualIncome,
          boolean employmentRequired
  ) {

    static EligibilityResponse from(Eligibility eligibility) {
      return new EligibilityResponse(
              eligibility.minAge(),
              eligibility.minAnnualIncome(),
              eligibility.employmentRequired()
      );
    }
  }

  public record Thresholds(
          int duplicateLoanMaxCount,
          int delinquencyLookbackMonths,
          int delinquencyMinOverdueDays,
          BigDecimal dsrLimitRate,
          long reductionUnitAmount,
          int loanTermYears
  ) {

    static Thresholds from(ScreeningThresholds thresholds) {
      return new Thresholds(
              thresholds.duplicateLoanMaxCount(),
              thresholds.delinquencyLookbackMonths(),
              thresholds.delinquencyMinOverdueDays(),
              thresholds.dsrLimitRate(),
              thresholds.reductionUnitAmount(),
              thresholds.loanTermYears()
      );
    }
  }
}
