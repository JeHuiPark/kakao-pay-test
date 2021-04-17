package kakao.pay.test.invest.impl.jpa;

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
