package kakao.pay.test.interfaces;

/**
 * 투자상품 요약정보.
 */
public interface InvestProductSummary extends InvestProduct {

  /**
   * 투자자 수.
   */
  long investorCount();

  /**
   * 누적 투자금액
   */
  long accumulatedInvestingAmount();
}
