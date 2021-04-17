package kakao.pay.test.interfaces;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * 투자 명령서.
 *
 * @see InvestingService 투자 서비스 (소비자)
 */
@Getter
public class InvestingCommand {

  private final long userId;

  private final long productId;

  private final long amount;

  @Builder
  private InvestingCommand(@NonNull Long userId,
                           @NonNull Long productId,
                           @NonNull Long amount) {
    this.userId = userId;
    this.productId = productId;
    this.amount = amount;
  }
}
