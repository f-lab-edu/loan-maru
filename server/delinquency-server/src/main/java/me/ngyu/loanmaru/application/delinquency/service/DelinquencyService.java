package me.ngyu.loanmaru.application.delinquency.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.delinquency.domain.AccountDelinquencies;
import me.ngyu.loanmaru.application.delinquency.domain.DelinquencyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DelinquencyService {

  private final DelinquencyRepository delinquencyRepository;

  public List<AccountDelinquencies> findDelinquencies(List<String> accountNos) {
    return accountNos.stream()
            .map(accountNo -> new AccountDelinquencies(
                    accountNo, delinquencyRepository.findAllByAccountNo(accountNo)))
            .toList();
  }
}
