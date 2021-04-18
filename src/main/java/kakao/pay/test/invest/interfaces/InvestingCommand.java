package kakao.pay.test.invest.interfaces;

import lombok.Builder;
import lombok.NonNull;

/**
 * 투자 명령서.
 *
 * @see InvestingService 투자 서비스 (소비자)
 */
public class InvestingCommand implements ProductInvestor {

  private final ProductInvestor productInvestor;

  private final long amount;

  @Builder
  private InvestingCommand(@NonNull ProductInvestor productInvestor,
                           @NonNull Long amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("투자금액은 0보다 작을 수 없습니다");
    }
    this.productInvestor = productInvestor;
    this.amount = amount;
  }

  public long amount() {
    return amount;
  }

  @Override
  public long userId() {
    return productInvestor.userId();
  }

  @Override
  public long productId() {
    return productInvestor.productId();
  }
}
