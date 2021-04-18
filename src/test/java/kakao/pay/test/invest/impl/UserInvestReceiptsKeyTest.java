package kakao.pay.test.invest.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @see UserInvestReceiptsKey
 */
@DisplayName("UserInvestReceiptsKeyTest")
class UserInvestReceiptsKeyTest {

  @Nested
  @DisplayName("생성자 deserialize")
  class Describe_deserialize {

    final String PREFIX = "user_invest:";

    @Nested
    @DisplayName("지원하는 형식의 문자열이 주어지면")
    class Context_case1 {

      @ValueSource(
          strings = {
              PREFIX + "1",
              PREFIX + "12",
              PREFIX + "123",
              PREFIX + "1234",
              PREFIX + Long.MAX_VALUE
          }
      )
      @ParameterizedTest
      @DisplayName("UserInvestReceiptsKey 를 리턴한다")
      void test2(String given) {
        var actual = UserInvestReceiptsKey.deserialize(given);

        assertTrue(UserInvestReceiptsKey.class.isAssignableFrom(actual.getClass()));
      }
    }

    @Nested
    @DisplayName("지원하지 않는 형식의 문자열이 주어지면")
    class Context_case2 {

      @ValueSource(
          strings = {
              PREFIX + "1a",
              PREFIX + "1 2",
              PREFIX + "123 ",
              "user_invest1234",
          }
      )
      @ParameterizedTest
      @DisplayName("IllegalArgumentException 예외가 발생한다")
      void test3(String given) {
        assertThrows(
            IllegalArgumentException.class,
            () -> UserInvestReceiptsKey.deserialize(given)
        );
      }
    }
  }
}
