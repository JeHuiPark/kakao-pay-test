package kakao.pay.test.invest.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductType;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import kakao.pay.test.invest.interfaces.ProductInvestor;
import kakao.pay.test.invest.interfaces.ProductInvestorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * @see InternalInvestRepository
 */
@DisplayName("InternalInvestRepositoryTest")
class InternalInvestRepositoryTest {

  @Import(InternalInvestRepository.class)
  @DataJpaTest
  private static class InternalInvestRepositoryTestContext {

    @Autowired
    InvestmentProductRepository investmentProductRepository;

    @Autowired
    InvestmentReceiptRepository investmentReceiptRepository;

    @Autowired
    ProductInvestmentLogRepository productInvestmentLogRepository;

    @Autowired
    InternalInvestRepository internalInvestRepository;

    InvestmentProduct preparedProduct;

    @BeforeEach
    private void _setupContext() {
      preparedProduct = InvestmentProduct.builder()
          .investPeriod(
              new InvestPeriod(
                  OffsetDateTime.now().minusDays(1),
                  OffsetDateTime.now().plusDays(1)))
          .investProductType(InvestProductType.CREDIT)
          .totalInvestingAmount(10_000_000L)
          .title("테스트 상품")
          .build();
      investmentProductRepository.saveAndFlush(preparedProduct);
    }
  }

  static abstract class SaveInvestProductReceiptTestContext extends InternalInvestRepositoryTestContext {

    InvestmentProduct subject(InvestmentReceipt given) {
      internalInvestRepository.saveInvestProductReceipt(given);
      return investmentProductRepository.findById(given.productId()).orElseThrow();
    }

    @BeforeEach
    private void setupContext() {
      if (preparedProductInvestor() == null) {
        return;
      }

      var investmentReceipt = preparedProduct.invest(
          InvestingCommand.builder()
              .productInvestor(preparedProductInvestor())
              .amount(500_000L)
              .build()
      );
      internalInvestRepository.saveInvestProductReceipt(investmentReceipt);
    }

    abstract ProductInvestor preparedProductInvestor();

    InvestmentReceipt given(InvestingCommand investingCommand) {
      return preparedProduct.invest(investingCommand);
    }
  }

  @Nested
  @DisplayName("saveInvestProductReceipt")
  class Describe_saveInvestProductReceipt {

    @Nested
    @DisplayName("투자정보가 기록된 적이 없으면")
    class Context_case1 extends SaveInvestProductReceiptTestContext {

      @Override
      ProductInvestor preparedProductInvestor() {
        return ProductInvestorUtil.build(preparedProduct.productId(), 1);
      }

      @Test
      @DisplayName("총 투자자수와 투자금액을 집계합니다")
      void test2() {
        var actual = subject(
            given(
                InvestingCommand.builder()
                    .productInvestor(ProductInvestorUtil.build(preparedProduct.productId(), 2))
                    .amount(10_000L)
                    .build()
            )
        );

        assertEquals(500_000 + 10_000, actual.accumulatedInvestingAmount());
        assertEquals(2, actual.investorCount());
      }
    }

    @Nested
    @DisplayName("투자정보가 기록된 적이 있으면")
    class Context_case2 extends SaveInvestProductReceiptTestContext {

      @Override
      ProductInvestor preparedProductInvestor() {
        return ProductInvestorUtil.build(preparedProduct.productId(), 3);
      }

      @Test
      @DisplayName("투자금액만 집계합니다")
      void test3() {
        var actual = subject(
            given(
                InvestingCommand.builder()
                    .productInvestor(preparedProductInvestor())
                    .amount(10_000L)
                    .build()

            )
        );
        assertEquals(500_000 + 10_000, actual.accumulatedInvestingAmount());
        assertEquals(1, actual.investorCount());
      }
    }
  }

  @Nested
  @DisplayName("findById")
  class Describe_findById {

    @Nested
    @DisplayName("주어진 ID와 일치하는 투자상품이 있으면")
    class Context_case1 extends InternalInvestRepositoryTestContext {

      @Test
      @DisplayName("상품정보를 리턴한다")
      void test5() {
        var actual = internalInvestRepository.findById(preparedProduct.productId());

        assertTrue(actual.isPresent());
      }
    }

    @Nested
    @DisplayName("주어진 ID와 일치하는 투자상품이 없으면")
    class Context_case2 extends InternalInvestRepositoryTestContext {

      @Test
      @DisplayName("빈 값을 리턴한다")
      void test6() {
        var actual = internalInvestRepository.findById(-999);

        assertTrue(actual.isEmpty());
      }
    }
  }
}
