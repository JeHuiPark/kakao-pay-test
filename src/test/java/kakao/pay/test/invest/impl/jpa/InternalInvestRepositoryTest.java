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
  private static class InternalInvestRepositoryTestContext extends PreparedInvestmentProductTestContext {

    @Autowired
    InvestmentReceiptRepository investmentReceiptRepository;

    @Autowired
    ProductInvestmentLogRepository productInvestmentLogRepository;

    @Autowired
    InternalInvestRepository internalInvestRepository;

    @Override
    InvestPeriod preparedProductPeriod() {
      return new InvestPeriod(
          OffsetDateTime.now().minusDays(1),
          OffsetDateTime.now().plusDays(1));
    }

    @Override
    long preparedProductTotalInvestingAmount() {
      return 10_000_000;
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
    @DisplayName("??????????????? ????????? ?????? ?????????")
    class Context_case1 extends SaveInvestProductReceiptTestContext {

      @Override
      ProductInvestor preparedProductInvestor() {
        return ProductInvestorUtil.build(preparedProduct.productId(), 1);
      }

      @Test
      @DisplayName("??? ??????????????? ??????????????? ???????????????")
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
    @DisplayName("??????????????? ????????? ?????? ?????????")
    class Context_case2 extends SaveInvestProductReceiptTestContext {

      @Override
      ProductInvestor preparedProductInvestor() {
        return ProductInvestorUtil.build(preparedProduct.productId(), 3);
      }

      @Test
      @DisplayName("??????????????? ???????????????")
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
    @DisplayName("????????? ID??? ???????????? ??????????????? ?????????")
    class Context_case1 extends InternalInvestRepositoryTestContext {

      @Test
      @DisplayName("??????????????? ????????????")
      void test5() {
        var actual = internalInvestRepository.findById(preparedProduct.productId());

        assertTrue(actual.isPresent());
      }
    }

    @Nested
    @DisplayName("????????? ID??? ???????????? ??????????????? ?????????")
    class Context_case2 extends InternalInvestRepositoryTestContext {

      @Test
      @DisplayName("??? ?????? ????????????")
      void test6() {
        var actual = internalInvestRepository.findById(-999);

        assertTrue(actual.isEmpty());
      }
    }
  }
}
