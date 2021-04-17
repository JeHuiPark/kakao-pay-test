package kakao.pay.test.invest.interfaces;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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

  /**
   * 투자기간이 유효할 경우 true 를 리턴합니다.
   */
  default boolean isEffectivePeriod() {
    return investPeriod().contains(OffsetDateTime.now(ZoneOffset.UTC));
  }
}
