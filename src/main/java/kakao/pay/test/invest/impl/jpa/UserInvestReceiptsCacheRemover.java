package kakao.pay.test.invest.impl.jpa;

import kakao.pay.test.invest.impl.InvestProductReceiptsCacheManager;
import kakao.pay.test.invest.impl.UserInvestReceiptsKey;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import kakao.pay.test.invest.interfaces.InvestingService;
import kakao.pay.test.invest.interfaces.exception.InvestingStateException;
import kakao.pay.test.invest.interfaces.exception.NotExistProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
class UserInvestReceiptsCacheRemover implements InvestingService {

  private final InvestingServiceImpl investingServiceImpl;
  private final InvestProductReceiptsCacheManager cacheManager;

  @Override
  public void investing(InvestingCommand investingCommand) throws InvestingStateException,
      NotExistProductException {
    investingServiceImpl.investing(investingCommand);

    var cacheKey = new UserInvestReceiptsKey(investingCommand.userId());
    try {
      cacheManager.purge(cacheKey);
    } catch (Exception e) {
      log.warn("'{}' 캐시 무효화에 실패 하였습니다.", cacheKey.key());
    }
  }
}
