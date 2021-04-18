package kakao.pay.test.invest.impl.redis;

import java.util.Optional;
import kakao.pay.test.invest.impl.InvestProductReceiptsCacheManager;
import kakao.pay.test.invest.impl.UserInvestReceipts;
import kakao.pay.test.invest.impl.UserInvestReceiptsKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class InvestProductReceiptCacheManagerImpl implements InvestProductReceiptsCacheManager {

  private final InvestProductReceiptsRepository investProductReceiptsRepository;

  @Override
  public void purge(UserInvestReceiptsKey key) {
    investProductReceiptsRepository.deleteById(key.key());
  }

  @Override
  public void set(UserInvestReceiptsKey key, UserInvestReceipts value) {
    var cache = new UserInvestReceiptsCache(key, value);
    investProductReceiptsRepository.save(cache);
  }

  @Override
  public Optional<UserInvestReceipts> get(UserInvestReceiptsKey key) {
    return investProductReceiptsRepository.findById(key.key())
        .map(UserInvestReceiptsCache::getValue);
  }
}
