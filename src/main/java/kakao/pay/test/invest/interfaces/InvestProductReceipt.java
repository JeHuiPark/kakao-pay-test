package kakao.pay.test.invest.interfaces;

/**
 * 투자 영수증.
 */
public interface InvestProductReceipt extends InvestProduct {

  /**
   * 투자금액.
   */
  long investingAmount();
}
