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
