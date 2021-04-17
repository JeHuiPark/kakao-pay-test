package kakao.pay.test.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * X-USER-ID 헤더에서 유저 식별자(long)를 추출합니다. <br>
 * 만약 유저정보가 누락 되었거나, 지원하지 않는 형식일 경우 오류로 처리합니다.
 *
 * @see UserIdResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserId {
}
