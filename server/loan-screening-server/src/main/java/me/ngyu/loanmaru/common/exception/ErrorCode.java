package me.ngyu.loanmaru.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다."),
  EXTERNAL_SYSTEM_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "외부 시스템을 사용할 수 없습니다."),
  THRESHOLD_VERSION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "적용 중인 기준값 버전이 없습니다.");

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
