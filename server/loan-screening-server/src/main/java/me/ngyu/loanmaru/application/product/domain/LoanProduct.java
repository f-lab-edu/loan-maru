package me.ngyu.loanmaru.application.product.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 기준값 버전에 속한 상품. 같은 상품 코드라도 버전마다 한도·금리·규칙이 다를 수 있다.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"threshold_version_id", "product_code"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "threshold_version_id")
  private ThresholdVersion thresholdVersion;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ProductCode productCode;

  @Column(nullable = false, length = 50)
  private String productName;

  private long maxLimitAmount;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal interestRate;

  private boolean dsrApplied;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "loan_product_rule", joinColumns = @JoinColumn(name = "loan_product_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "rule_code", nullable = false, length = 30)
  private Set<ScreeningRule> appliedRules = new HashSet<>();

  @Embedded
  private Eligibility eligibility;

  /**
   * 적용 규칙을 {@link ScreeningRule} 선언 순서로 돌려준다.
   */
  public List<ScreeningRule> getAppliedRules() {
    return appliedRules.stream().sorted().toList();
  }
}
