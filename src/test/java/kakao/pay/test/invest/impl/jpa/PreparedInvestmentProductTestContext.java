package kakao.pay.test.invest.impl.jpa;

import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 테스트용 투자상품을 준비시킵니다.
 */
abstract class PreparedInvestmentProductTestContext {

  @Autowired
  InvestmentProductRepository investmentProductRepository;

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
}
