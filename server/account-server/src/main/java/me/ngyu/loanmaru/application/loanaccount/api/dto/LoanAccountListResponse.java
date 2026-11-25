package me.ngyu.loanmaru.application.loanaccount.api.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import me.ngyu.loanmaru.application.loanaccount.domain.LoanAccount;

/**
 * 계정계 전문 형식의 응답. 필드 이름은 계정계 명세를 그대로 따른다.
 */
public record LoanAccountListResponse(
        String resultCode,
        String resultMsg,
        List<Account> acctList
) {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

  public static LoanAccountListResponse success(List<LoanAccount> accounts) {
    return new LoanAccountListResponse("0000", "정상", accounts.stream().map(Account::from).toList());
  }

  public record Account(
          String acctNo,
          String custCi,
          String prdtTpCd,
          String acctStCd,
          long loanAmt,
          long loanBal,
          BigDecimal intRt,
          long annRpyAmt,
          String loanStrDt,
          String mtrtDt,
          String chnlCd
  ) {

    static Account from(LoanAccount account) {
      return new Account(
              account.accountNo(),
              account.custCi(),
              account.productTypeCode(),
              account.accountStatusCode(),
              account.loanAmount(),
              account.loanBalance(),
              account.interestRate(),
              account.annualRepaymentAmount(),
              account.loanStartDate().format(DATE_FORMAT),
              account.maturityDate().format(DATE_FORMAT),
              account.channelCode()
      );
    }
  }
}
