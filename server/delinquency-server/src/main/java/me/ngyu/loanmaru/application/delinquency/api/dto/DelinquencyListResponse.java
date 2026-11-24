package me.ngyu.loanmaru.application.delinquency.api.dto;

import java.time.LocalDate;
import java.util.List;
import me.ngyu.loanmaru.application.delinquency.domain.AccountDelinquencies;
import me.ngyu.loanmaru.application.delinquency.domain.Delinquency;

public record DelinquencyListResponse(List<Result> results) {

  public static DelinquencyListResponse from(List<AccountDelinquencies> accounts) {
    return new DelinquencyListResponse(accounts.stream().map(Result::from).toList());
  }

  public record Result(String acctNo, List<Item> delinquencies) {

    static Result from(AccountDelinquencies account) {
      return new Result(
              account.accountNo(),
              account.delinquencies().stream().map(Item::from).toList()
      );
    }
  }

  public record Item(
          String delinquencyId,
          LocalDate startDate,
          LocalDate endDate,
          int overdueDays
  ) {

    static Item from(Delinquency delinquency) {
      return new Item(
              delinquency.delinquencyId(),
              delinquency.startDate(),
              delinquency.endDate(),
              delinquency.overdueDays()
      );
    }
  }
}
