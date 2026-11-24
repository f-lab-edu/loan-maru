package me.ngyu.loanmaru.application.externalloan.service;

import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoanRepository;
import me.ngyu.loanmaru.application.externalloan.domain.ExternalLoanSnapshot;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalLoanService {

  private final ExternalLoanRepository externalLoanRepository;

  public ExternalLoanSnapshot inquire(String custCi) {
    return externalLoanRepository.findSnapshotByCustCi(custCi);
  }
}
