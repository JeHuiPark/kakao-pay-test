package kakao.pay.test.invest.impl;

import kakao.pay.test.invest.interfaces.ProductInvestor;

/**
 * 상품 투자자 로거.
 */
public interface ProductInvestorLogger {

  /**
   * 상품 투자자 정보를 기록 합니다.
   * @param productInvestor 기록 대상
   * @return 기록된 적이 없으면 true 를 리턴합니다.
   */
  boolean logging(ProductInvestor productInvestor);
}
