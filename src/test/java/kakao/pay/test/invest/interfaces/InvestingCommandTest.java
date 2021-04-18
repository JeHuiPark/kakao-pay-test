package kakao.pay.test.invest.interfaces;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @see InvestingCommand
 */
@DisplayName("InvestingCommandTest")
class InvestingCommandTest {

  @Nested
  @DisplayName("생성자")
  class Describe_constructor {

    @Nested
    @DisplayName("투자금액이 0보다 작으면")
    class Context_case1 {

      @ValueSource(longs = {-1, Long.MIN_VALUE})
      @ParameterizedTest
      @DisplayName("IllegalArgumentException 예외가 발생한다")
      void test3(long given) {
        assertThrows(
            IllegalArgumentException.class,
            () -> InvestingCommand.builder()
                .productInvestor(ProductInvestorUtil.build(1, 1))
                .amount(given)
                .build()
        );
      }
    }
  }
}
