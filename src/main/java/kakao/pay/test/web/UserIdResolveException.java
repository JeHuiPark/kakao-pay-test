package kakao.pay.test.web;

/**
 * UserId 데이터 처리불가.
 */
public class UserIdResolveException extends RuntimeException {

  UserIdResolveException(String message) {
    super(message);
  }

  UserIdResolveException(String message, Throwable cause) {
    super(message, cause);
  }
}
