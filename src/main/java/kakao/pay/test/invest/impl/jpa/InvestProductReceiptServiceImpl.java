package kakao.pay.test.invest.impl.jpa;

import java.util.List;
import java.util.stream.Collectors;
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

  @Override
  public List<InvestProductReceipt> findAllByUserId(long userId) {
    return investmentReceiptRepository.findAllByUserIdOrderByInvestingAtDesc(userId)
        .map(e -> (InvestProductReceipt) e)
        .collect(Collectors.toList());
  }
}
