package kakao.pay.test.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @see UserIdResolver
 */
class UserIdResolverTest {

  @AutoConfigureMockMvc
  @Import(CommonWebConfiguration.class)
  @WebMvcTest(UserIdResolverTest.TestController.class)
  private static class UserIdResolverTestContext {

    static final String HEADER = "X-USER-ID";

    @Autowired
    MockMvc mockMvc;

    RequestBuilder buildRequest(String headerValue) {
      var builder = get("/echo");
      if ("null".equals(headerValue)) {
        return builder;
      }
      return builder.header(HEADER, headerValue);
    }
  }

  @Nested
  @DisplayName("X-USER-ID 헤더가 누락됐거나, 지원하지 않는 형식의 데이터가 입력되면")
  class Context_case1 extends UserIdResolverTestContext {

    @ValueSource(
        strings = {
            "1a",
            "aa",
            "1 ",
            "1 ",
            " 123 ",
            "",
            "null"
        }
    )
    @ParameterizedTest
    @DisplayName("예외처리 합니다")
    void test3(String given) throws Exception {
      mockMvc.perform(buildRequest(given))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("X-USER-ID 헤더에 지원하는 형식의 데이터가 입력되면")
  class Context_case2 extends UserIdResolverTestContext {

    @ValueSource(
        strings = {
            "1234",
            "1",
            "999999",
        }
    )
    @ParameterizedTest
    @DisplayName("userId를 추출하여 메소드 파라미터로 전달합니다")
    void test4(String given) throws Exception {
      mockMvc.perform(buildRequest(given))
          .andExpect(status().isOk())
          .andExpect(jsonPath("userId").value(given));
    }
  }

  @RestController
  static class TestController {

    @GetMapping("/echo")
    public Map<String, Object> echo(@UserId long userId) {
      return Map.of("userId" , userId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    void handleUserIdResolveException(UserIdResolveException e) {
      // no data
    }
  }
}
