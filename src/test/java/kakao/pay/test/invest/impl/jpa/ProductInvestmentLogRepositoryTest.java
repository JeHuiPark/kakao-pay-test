package kakao.pay.test.invest.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kakao.pay.test.invest.interfaces.ProductInvestor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * @see ProductInvestmentLogRepository
 */
@DisplayName("ProductInvestmentLogRepository")
class ProductInvestmentLogRepositoryTest {

  @DataJpaTest
  private abstract static class AccumulateTestContext {

    @Autowired
    ProductInvestmentLogRepository productInvestmentLogRepository;

    @BeforeEach
    void setupContext() {
      if (preparedProductInvestLog() == null) {
        return;
      }
      var preparedData = new ProductInvestmentLog(preparedProductInvestLog());
      productInvestmentLogRepository.saveAndFlush(preparedData);
    }

    abstract ProductInvestor preparedProductInvestLog();

    boolean subject(ProductInvestor given) {
      return productInvestmentLogRepository.logging(given);
    }

    ProductInvestor build(long productId, long userId) {
      return new ProductInvestor() {
        @Override
        public long userId() {
          return userId;
        }

        @Override
        public long productId() {
          return productId;
        }
      };
    }
  }

  @Nested
  @DisplayName("logging")
  class Describe_logging {

    @Nested
    @DisplayName("상품투자 기록이 있으면")
    class Context_case1 extends AccumulateTestContext {

      ProductInvestor given = build(1, 1);

      @Override
      ProductInvestor preparedProductInvestLog() {
        return given;
      }

      @Test
      @DisplayName("false 를 리턴한다")
      void test2() {
        var actual = subject(given);

        assertFalse(actual);
      }
    }

    @Nested
    @DisplayName("상품투자 기록이 없으면")
    class Context_case2 extends AccumulateTestContext {

      ProductInvestor given = build(1, 1);

      @Override
      ProductInvestor preparedProductInvestLog() {
        return null;
      }

      @Test
      @DisplayName("true 를 리턴한다")
      void test2() {
        var actual = subject(given);

        assertTrue(actual);
      }
    }
  }
}
