package kakao.pay.test.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
class UserIdResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(UserId.class) != null;
  }

  @Override
  public Long resolveArgument(MethodParameter parameter,
                              ModelAndViewContainer mavContainer,
                              NativeWebRequest webRequest,
                              WebDataBinderFactory binderFactory) {
    var userId = webRequest.getHeader("X-USER-ID");
    if (userId == null) {
      log.info("X-USER-ID 정보가 누락됐습니다.");
      throw new UserIdResolveException("잘못된 요청입니다.");
    }

    try {
      return Long.parseLong(userId);
    } catch (NumberFormatException e) {
      log.info("입력된 X-USER-ID 정보'{}'가 형식에 맞지 않습니다.", userId);
      throw new UserIdResolveException("잘못된 요청입니다.", e);
    }
  }
}
