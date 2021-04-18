package kakao.pay.test.invest.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import kakao.pay.test.invest.impl.InvestProductReceiptsCacheManager;
import kakao.pay.test.invest.impl.UserInvestReceipts;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import kakao.pay.test.invest.interfaces.ProductInvestorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

/**
 * @see InvestProductReceiptServiceImpl
 */
@DisplayName("InvestProductReceiptServiceImplTest")
class InvestProductReceiptServiceImplTest {

  @Import({
      InternalInvestRepository.class,
      InvestingServiceImpl.class,
      InvestProductReceiptServiceImpl.class,
  })
  @DataJpaTest
  private abstract static class FindAllByUserIdTestContext extends PreparedInvestmentProductTestContext {

    @Autowired
    InvestProductReceiptServiceImpl investProductReceiptService;

    @Autowired
    InvestingServiceImpl investingServiceImpl;

    @Autowired
    InvestProductReceiptServiceImpl investProductReceiptServiceImpl;

    @MockBean
    InvestProductReceiptsCacheManager investProductReceiptsCacheManager;

    @Override
    InvestPeriod preparedProductPeriod() {
      return new InvestPeriod(
          OffsetDateTime.now().minusDays(1),
          OffsetDateTime.now().plusDays(1));
    }

    @Override
    long preparedProductTotalInvestingAmount() {
      return 10_000;
    }

    List<InvestProductReceipt> subject(long given) {
      return investProductReceiptServiceImpl.findAllByUserId(given);
    }
  }

  @Nested
  @DisplayName("findAllByUserId")
  class Describe_findAllByUserId {

    @Nested
    @DisplayName("해당 사용자의 투자정보가 존재하면")
    class Context_case1 extends FindAllByUserIdTestContext {

      long givenUserId = 1;

      @BeforeEach
      void setupContext() {
        investingServiceImpl.investing(
            InvestingCommand.builder()
                .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), givenUserId))
                .amount(3_000L)
                .build()
        );
      }

      @Test
      @DisplayName("투자목록을 리턴합니다")
      void test2() {
        var actual = subject(givenUserId);

        assertEquals(1, actual.size());
      }
    }

    @Nested
    @DisplayName("해당 사용자의 투자정보가 없으면")
    class Context_case2 extends FindAllByUserIdTestContext {

      @Test
      @DisplayName("빈 목록을 리턴한다")
      void test3() {
        var actual = subject(1);

        assertTrue(actual.isEmpty());
      }
    }

    @Nested
    @DisplayName("캐싱된 데이터가 존재하면")
    class Context_case3 extends FindAllByUserIdTestContext {

      @BeforeEach
      void setupContext() {
        when(investProductReceiptsCacheManager.get(any()))
            .thenReturn(Optional.of(new UserInvestReceipts(List.of())));
      }

      @Test
      @DisplayName("캐싱된 데이터를 리턴합니다")
      void test5() {
        var actual = subject(1);

        assertEquals(0, actual.size());
        verify(investProductReceiptsCacheManager, times(1)).get(any());
        verify(investProductReceiptsCacheManager, never()).set(any(), any());
      }
    }

    @Nested
    @DisplayName("캐싱된 데이터가 없으면")
    class Context_case4 extends FindAllByUserIdTestContext {

      long givenUserId = 1;

      @BeforeEach
      void setupContext() {
        investingServiceImpl.investing(
            InvestingCommand.builder()
                .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), givenUserId))
                .amount(3_000L)
                .build()
        );
        when(investProductReceiptsCacheManager.get(any()))
            .thenReturn(Optional.empty());
      }

      @Test
      @DisplayName("캐시 데이터를 생성하고, 데이터를 리턴합니다")
      void test6() {
        var actual = subject(givenUserId);

        assertEquals(1, actual.size());
        verify(investProductReceiptsCacheManager, times(1)).get(any());
        verify(investProductReceiptsCacheManager, times(1)).set(any(), any());
      }
    }
  }
}
