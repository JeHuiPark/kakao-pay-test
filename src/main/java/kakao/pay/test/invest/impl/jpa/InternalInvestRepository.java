package kakao.pay.test.invest.impl.jpa;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class InternalInvestRepository {

  private final InvestmentProductRepository investmentProductRepository;
  private final InvestmentReceiptRepository investmentReceiptRepository;
  private final ProductInvestmentLogRepository productInvestmentLogRepository;

  public Optional<InvestmentProduct> findById(long productId) {
    return investmentProductRepository.findById(productId);
  }

  public void saveInvestProductReceipt(InvestmentReceipt investProductReceipt) {
    investmentReceiptRepository.saveAndFlush(investProductReceipt);
    if (productInvestmentLogRepository.logging(investProductReceipt)) {
      investmentProductRepository.accumulateInvestmentAmountWithInvestor(
          investProductReceipt.productId(), investProductReceipt.investingAmount());
    } else {
      investmentProductRepository.accumulateInvestmentAmount(
          investProductReceipt.productId(), investProductReceipt.investingAmount());
    }
  }
}
