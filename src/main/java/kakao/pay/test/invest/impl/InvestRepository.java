package kakao.pay.test.invest.impl;

import java.util.Optional;
import kakao.pay.test.invest.interfaces.InvestProduct;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;

public interface InvestRepository {

  /**
   * 주어진 productId 와 일치하는 투자상품 요약정보를 리턴합니다.
   *
   * @param productId 투자상품 식별자
   */
  Optional<? extends InvestProduct> findById(long productId);

  /**
   * 투자 정보를 저장합니다
   * @param investProductReceipt 투자 영수증
   */
  void saveInvestProductReceipt(InvestProductReceipt investProductReceipt);
}
