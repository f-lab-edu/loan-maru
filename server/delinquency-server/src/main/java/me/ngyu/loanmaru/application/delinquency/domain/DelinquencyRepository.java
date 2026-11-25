package me.ngyu.loanmaru.application.delinquency.domain;

import java.util.List;

public interface DelinquencyRepository {

  List<Delinquency> findAllByAccountNo(String accountNo);
}
