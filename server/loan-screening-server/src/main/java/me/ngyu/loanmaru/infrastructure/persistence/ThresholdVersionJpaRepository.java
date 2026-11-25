package me.ngyu.loanmaru.infrastructure.persistence;

import java.time.Instant;
import java.util.Optional;
import me.ngyu.loanmaru.application.product.domain.ThresholdVersion;
import org.springframework.data.jpa.repository.JpaRepository;

interface ThresholdVersionJpaRepository extends JpaRepository<ThresholdVersion, Long> {

  Optional<ThresholdVersion> findFirstByEffectiveFromLessThanEqualOrderByEffectiveFromDesc(Instant now);
}
