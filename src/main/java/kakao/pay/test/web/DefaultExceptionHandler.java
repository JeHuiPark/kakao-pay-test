package kakao.pay.test.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * API 요청 처리중 예외 발생시 기본 동작을 정의합니다.
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    String message;
    if (ex.getBindingResult().getFieldError() == null) {
      message = "N/A";
    } else {
      message = ex.getBindingResult().getFieldError().getDefaultMessage();
    }
    return ResponseEntity
        .badRequest()
        .body(
            ErrorResponse.builder()
                .message(message)
                .build()
        );
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserIdResolveException.class)
  ErrorResponse handleUserIdResolveException(UserIdResolveException e) {
    return ErrorResponse.builder()
        .message("사용자 정보가 누락됐습니다.")
        .build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  ErrorResponse handleException(Exception e) {
    log.error("제어하지 못한 오류 발생", e);
    return ErrorResponse.builder()
        .message("처리도중 알 수 없는 문제가 발생하였습니다")
        .build();
  }
}
