package kakao.pay.test.invest.interfaces;

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

  /**
   * 투자상품이 매진됐을 경우에 true 를 리턴한다.
   */
  default boolean isSoldOut() {
    return totalInvestingAmount() == accumulatedInvestingAmount();
  }
}
