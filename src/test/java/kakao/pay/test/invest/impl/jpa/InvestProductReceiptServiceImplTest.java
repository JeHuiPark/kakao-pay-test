package kakao.pay.test.invest.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.util.List;
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
        var actual = subject(1);

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
  }
}
