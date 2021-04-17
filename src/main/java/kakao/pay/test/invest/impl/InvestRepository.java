package kakao.pay.test.invest.impl;

import kakao.pay.test.invest.interfaces.InvestProductReceipt;

public interface InvestRepository {

  /**
   * 투자 정보를 저장합니다
   * @param investProductReceipt 투자 영수증
   */
  void saveInvestProductReceipt(InvestProductReceipt investProductReceipt);
}
