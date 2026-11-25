package me.ngyu.loanmaru.application.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통 기준값과 상품 구성을 묶은 버전. 적용 시작 시각이 지난 버전 중 가장 최근 것이 현재 버전이다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThresholdVersion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 30)
  private String versionKey;

  @Embedded
  private ScreeningThresholds thresholds;

  @Column(nullable = false)
  private Instant effectiveFrom;

  @OneToMany(mappedBy = "thresholdVersion")
  @OrderBy("productCode")
  private List<LoanProduct> products = new ArrayList<>();
}
