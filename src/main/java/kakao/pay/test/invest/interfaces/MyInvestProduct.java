package kakao.pay.test.invest.interfaces;

/**
 * 나의 투자상품
 */
public interface MyInvestProduct extends InvestProduct {

  /**
   * 니의 투자금액.
   */
  long myInvestingAmount();
}
