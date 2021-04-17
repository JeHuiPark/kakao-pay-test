package kakao.pay.test.invest.impl.jpa;

import kakao.pay.test.invest.interfaces.InvestingCommand;
import kakao.pay.test.invest.interfaces.InvestingService;
import kakao.pay.test.invest.interfaces.exception.InvestingPeriodException;
import kakao.pay.test.invest.interfaces.exception.InvestingStateException;
import kakao.pay.test.invest.interfaces.exception.NotExistProductException;
import kakao.pay.test.invest.interfaces.exception.SoldOutException;
import kakao.pay.test.invest.interfaces.exception.TotalAmountExceedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
class InvestingServiceImpl implements InvestingService {

  private final InternalInvestRepository investRepository;

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  @Override
  public void investing(InvestingCommand investingCommand) throws InvestingStateException,
      NotExistProductException {
    var investmentProduct = getInvestmentProduct(investingCommand.productId());
    ensure(investmentProduct);

    simulation(investmentProduct, investingCommand);
    var investmentReceipt = investmentProduct.invest(investingCommand);
    investRepository.saveInvestProductReceipt(investmentReceipt);

    investmentProduct = getInvestmentProduct(investingCommand.productId());
    if (investmentProduct.isOverflow()) {
      throw new TotalAmountExceedException();
    }
  }

  private InvestmentProduct getInvestmentProduct(long productId) {
    return investRepository.findById(productId)
        .orElseThrow(NotExistProductException::new);
  }

  private void ensure(InvestmentProduct investmentProduct) {
    if (!investmentProduct.isEffectivePeriod()) {
      throw new InvestingPeriodException();
    }

    if (investmentProduct.isSoldOut()) {
      throw new SoldOutException();
    }
  }

  private void simulation(InvestmentProduct investmentProduct, InvestingCommand investingCommand) {
    if (investmentProduct.isOverflow(investingCommand.amount())) {
      log.info("시뮬레이션 결과 총 모집금액을 초과할 것으로 예상되어 트랜잭션을 취소합니다");
      throw new TotalAmountExceedException();
    }
  }
}
