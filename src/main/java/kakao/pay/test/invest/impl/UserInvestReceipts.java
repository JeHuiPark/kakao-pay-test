package kakao.pay.test.invest.impl;

import java.util.List;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 캐시 Value.
 *
 * @see CacheManager
 * @see InvestProductReceiptsCacheManager
 */
@Getter
@RequiredArgsConstructor
public class UserInvestReceipts {
  private final List<InvestProductReceipt> getReceipts;
}
