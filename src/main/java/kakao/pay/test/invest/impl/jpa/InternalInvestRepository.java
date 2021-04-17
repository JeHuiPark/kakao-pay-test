package kakao.pay.test.invest.impl.jpa;

import java.util.Optional;
import kakao.pay.test.invest.impl.InvestRepository;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class InternalInvestRepository implements InvestRepository {

  private final InvestmentProductRepository investmentProductRepository;
  private final InvestmentReceiptRepository investmentReceiptRepository;
  private final ProductInvestmentLogRepository productInvestmentLogRepository;

  @Override
  public Optional<InvestmentProduct> findById(long productId) {
    return investmentProductRepository.findById(productId);
  }

  @Override
  public void saveInvestProductReceipt(InvestProductReceipt investProductReceipt) {
    investmentReceiptRepository.saveAndFlush((InvestmentReceipt) investProductReceipt);
    if (productInvestmentLogRepository.logging(investProductReceipt)) {
      investmentProductRepository.accumulateInvestmentAmountWithInvestor(
          investProductReceipt.productId(), investProductReceipt.investingAmount());
    } else {
      investmentProductRepository.accumulateInvestmentAmount(
          investProductReceipt.productId(), investProductReceipt.investingAmount());
    }
  }
}
