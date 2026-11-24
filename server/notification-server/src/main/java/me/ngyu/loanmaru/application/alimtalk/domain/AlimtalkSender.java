package me.ngyu.loanmaru.application.alimtalk.domain;

public interface AlimtalkSender {

  /**
   * 알림톡 발송을 접수하고 발송 식별자를 돌려준다. 접수 성공이 실제 도달을 보장하지는 않는다.
   */
  String send(Alimtalk alimtalk);
}
