package kakao.pay.test.invest.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductType;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import kakao.pay.test.invest.interfaces.ProductInvestorUtil;
import kakao.pay.test.invest.interfaces.exception.InvestingPeriodException;
import kakao.pay.test.invest.interfaces.exception.NotExistProductException;
import kakao.pay.test.invest.interfaces.exception.SoldOutException;
import kakao.pay.test.invest.interfaces.exception.TotalAmountExceedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * @see InvestingServiceImpl
 */
@DisplayName("InvestingServiceImplTest")
class InvestingServiceImplTest {

  @Import({
      InternalInvestRepository.class,
      InvestingServiceImpl.class
  })
  @DataJpaTest
  private abstract static class InvestingTestContext {

    @Autowired
    InvestmentProductRepository investmentProductRepository;

    @Autowired
    InvestingServiceImpl investingServiceImpl;

    InvestmentProduct preparedProduct;

    @BeforeEach
    void _setupContext() {
      preparedProduct = InvestmentProduct.builder()
          .investPeriod(preparedProductPeriod())
          .investProductType(InvestProductType.CREDIT)
          .totalInvestingAmount(preparedProductTotalInvestingAmount())
          .title("테스트 상품")
          .build();
      investmentProductRepository.saveAndFlush(preparedProduct);
    }

    abstract InvestPeriod preparedProductPeriod();

    abstract long preparedProductTotalInvestingAmount();

    InvestmentProduct subject(InvestingCommand given) {
      investingServiceImpl.investing(given);
      return investmentProductRepository.findById(given.productId()).orElseThrow();
    }
  }

  @Nested
  @DisplayName("investing")
  class Describe_investing {

    @Nested
    @DisplayName("주어진 명령서와 일치하는 투자상품이 없으면")
    class Context_case1 extends InvestingTestContext {

      @Override
      InvestPeriod preparedProductPeriod() {
        return new InvestPeriod(
            OffsetDateTime.now().minusDays(1),
            OffsetDateTime.now().plusDays(1)
        );
      }

      @Override
      long preparedProductTotalInvestingAmount() {
        return 0;
      }

      InvestingCommand given() {
        return InvestingCommand.builder()
            .productInvestor(ProductInvestorUtil.build(-999, 1))
            .amount(100L)
            .build();
      }

      @Test
      @DisplayName("NotExistProductException 예외가 발생합니다")
      void test2() {
        assertThrows(
            NotExistProductException.class,
            () -> subject(given())
        );
      }
    }

    @Nested
    @DisplayName("투자기간이 아니면")
    class Context_case2 extends InvestingTestContext {

      @Override
      InvestPeriod preparedProductPeriod() {
        return new InvestPeriod(
            OffsetDateTime.now().minusDays(2),
            OffsetDateTime.now().minusDays(1)
        );
      }

      @Override
      long preparedProductTotalInvestingAmount() {
        return 0;
      }

      InvestingCommand given() {
        return InvestingCommand.builder()
            .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), 1))
            .amount(100L)
            .build();
      }

      @Test
      @DisplayName("InvestingPeriodException 예외가 발생합니다")
      void test4() {
        assertThrows(
            InvestingPeriodException.class,
            () -> subject(given())
        );
      }
    }

    @Nested
    @DisplayName("총 투자금액이 총 모집금액에 달성 했으면")
    class Context_case3 extends InvestingTestContext {

      @Override
      InvestPeriod preparedProductPeriod() {
        return new InvestPeriod(
            OffsetDateTime.now().minusDays(1),
            OffsetDateTime.now().plusDays(1)
        );
      }

      @Override
      long preparedProductTotalInvestingAmount() {
        return 10_000;
      }

      @BeforeEach
      void setupContext() {
        investingServiceImpl.investing(
            InvestingCommand.builder()
                .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), 1))
                .amount(preparedProductTotalInvestingAmount())
                .build()
        );
      }

      InvestingCommand given() {
        return InvestingCommand.builder()
            .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), 1))
            .amount(100L)
            .build();
      }

      @Test
      @DisplayName("SoldOutException 예외가 발생한다")
      void test5() {
        assertThrows(
            SoldOutException.class,
            () -> subject(given())
        );
      }
    }

    @Nested
    @DisplayName("총 투자금액이 총 모집금액을 초과하면")
    class Context_case4 extends InvestingTestContext {

      long remainInvestingAmount = 1000;

      @Override
      InvestPeriod preparedProductPeriod() {
        return new InvestPeriod(
            OffsetDateTime.now().minusDays(1),
            OffsetDateTime.now().plusDays(1)
        );
      }

      @Override
      long preparedProductTotalInvestingAmount() {
        return 10_000;
      }

      @BeforeEach
      void setupContext() {
        investingServiceImpl.investing(
            InvestingCommand.builder()
                .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), 1))
                .amount(preparedProductTotalInvestingAmount() - remainInvestingAmount)
                .build()
        );
      }

      InvestingCommand given() {
        return InvestingCommand.builder()
            .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), 1))
            .amount(remainInvestingAmount + 100)
            .build();
      }

      @Test
      @DisplayName("TotalAmountExceedException 예외가 발생한다")
      void test6() {
        assertThrows(
            TotalAmountExceedException.class,
            () -> subject(given())
        );
      }
    }

    @Nested
    @DisplayName("모든 조건을 만족하면")
    class Context_case5 extends InvestingTestContext {

      @Override
      InvestPeriod preparedProductPeriod() {
        return new InvestPeriod(
            OffsetDateTime.now().minusDays(1),
            OffsetDateTime.now().plusDays(1)
        );
      }

      @Override
      long preparedProductTotalInvestingAmount() {
        return 10_000;
      }

      InvestingCommand given() {
        return InvestingCommand.builder()
            .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), 1))
            .amount(preparedProductTotalInvestingAmount())
            .build();
      }

      @Test
      @DisplayName("정상 리턴한다")
      void test7() {
        var actual = subject(given());

        assertTrue(actual.isSoldOut());
        assertEquals(1, actual.investorCount());
      }
    }
  }
}
