package kakao.pay.test.invest.interfaces;

/**
 * 투자상품.
 */
public interface InvestProduct {

  /**
   * 상품 ID.
   */
  long productId();

  /**
   * 상품 제목.
   */
  String title();

  /**
   * 총 모집금액.
   */
  long totalInvestingAmount();

  /**
   * 모집기간.
   */
  InvestPeriod investPeriod();

  /**
   * 투자상품 종류.
   */
  InvestProductType investProductType();
}
