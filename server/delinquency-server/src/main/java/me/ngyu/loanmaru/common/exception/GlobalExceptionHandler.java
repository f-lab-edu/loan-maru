package me.ngyu.loanmaru.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    if (errorCode.getStatus().is5xxServerError()) {
      log.error("서버 오류가 발생했습니다. code={}", errorCode.name(), exception);
    }
    return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler({
          MethodArgumentNotValidException.class,
          HttpMessageNotReadableException.class,
          MissingServletRequestParameterException.class
  })
  public ResponseEntity<ErrorResponse> handleValidationException(Exception exception) {
    ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
    return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.from(errorCode));
  }
}
