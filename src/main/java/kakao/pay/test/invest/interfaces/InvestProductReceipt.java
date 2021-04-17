package kakao.pay.test.invest.interfaces;

import java.time.OffsetDateTime;

/**
 * 투자 영수증.
 */
public interface InvestProductReceipt extends InvestProduct {

  /**
   * 투자금액.
   */
  long investingAmount();

  /**
   * 투자일시.
   */
  OffsetDateTime investingAt();
}
