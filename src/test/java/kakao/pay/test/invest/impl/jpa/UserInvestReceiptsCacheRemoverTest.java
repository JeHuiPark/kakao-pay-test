package kakao.pay.test.invest.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import kakao.pay.test.invest.impl.InvestProductReceiptsCacheManager;
import kakao.pay.test.invest.impl.UserInvestReceiptsKey;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import kakao.pay.test.invest.interfaces.ProductInvestorUtil;
import kakao.pay.test.invest.interfaces.exception.InvestingPeriodException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * @see UserInvestReceiptsCacheRemover
 */
@DisplayName("UserInvestReceiptsCacheRemoverTest")
class UserInvestReceiptsCacheRemoverTest {

  private static class InvestingTestContext {

    InvestingServiceImpl investingService = mock(InvestingServiceImpl.class);
    InvestProductReceiptsCacheManager cacheManager = mock(InvestProductReceiptsCacheManager.class);
    UserInvestReceiptsCacheRemover cacheRemover =
        new UserInvestReceiptsCacheRemover(investingService, cacheManager);

    void subject(InvestingCommand given) {
      cacheRemover.investing(given);
    }

    InvestingCommand given() {
      return InvestingCommand.builder()
          .productInvestor(ProductInvestorUtil.build(1, 1))
          .amount(1000L)
          .build();
    }
  }

  @Nested
  @DisplayName("investing")
  class Describe_investing {

    @Nested
    @DisplayName("투자하기에 성공하면")
    class Context_case1 extends InvestingTestContext {

      @Test
      @DisplayName("UserInvestReceipts 캐시를 제거합니다")
      void test2() {
        subject(given());

        var purgeKeyCaptor = ArgumentCaptor.forClass(UserInvestReceiptsKey.class);
        verify(cacheManager, times(1)).purge(purgeKeyCaptor.capture());
        assertEquals(given().userId(), purgeKeyCaptor.getValue().getUserId());
      }
    }

    @Nested
    @DisplayName("투자하기도중 예외가 발생하면")
    class Context_case2 extends InvestingTestContext {

      @BeforeEach
      void setupContext() {
        doThrow(InvestingPeriodException.class)
            .when(investingService).investing(any());
      }

      @Test
      @DisplayName("발생한 예외를 제어하지 않고 상위 스택으로 전달합니다")
      void test3() {
        assertThrows(
            InvestingPeriodException.class,
            () -> subject(given())
        );
      }
    }

    @Nested
    @DisplayName("캐시 제거중 예외가 발생하면")
    class Context_case3 extends InvestingTestContext {

      @BeforeEach
      void setupContext() {
        doThrow(RuntimeException.class)
            .when(cacheManager).purge(any());
      }

      @Test
      @DisplayName("중단하지 않고, 정상 리턴합니다")
      void test4() {
        subject(given());
      }
    }
  }
}
