package kakao.pay.test.invest.impl.jpa;

import java.util.List;
import java.util.stream.Collectors;
import kakao.pay.test.invest.impl.InvestProductReceiptsCacheManager;
import kakao.pay.test.invest.impl.UserInvestReceipts;
import kakao.pay.test.invest.impl.UserInvestReceiptsKey;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import kakao.pay.test.invest.interfaces.InvestProductReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class InvestProductReceiptServiceImpl implements InvestProductReceiptService {

  private final InvestmentReceiptRepository investmentReceiptRepository;
  private final InvestProductReceiptsCacheManager investProductReceiptsCacheManager;

  @Override
  public List<InvestProductReceipt> findAllByUserId(long userId) {
    var cacheKey = new UserInvestReceiptsKey(userId);
    var cache = investProductReceiptsCacheManager.get(cacheKey);
    if (cache.isPresent()) {
      return cache.get().getGetReceipts();
    }

    var data = investmentReceiptRepository.findAllByUserIdOrderByInvestingAtDesc(userId)
        .map(e -> (InvestProductReceipt) e)
        .collect(Collectors.toList());
    investProductReceiptsCacheManager.set(cacheKey, new UserInvestReceipts(data));
    return data;
  }
}
