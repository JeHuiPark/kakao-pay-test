package kakao.pay.test.invest.impl.jpa;

import java.time.OffsetDateTime;
import java.util.List;
import kakao.pay.test.invest.interfaces.InvestProductSummary;
import kakao.pay.test.invest.interfaces.InvestProductSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class InvestProductSummaryServiceImpl implements InvestProductSummaryService {

  private final InvestmentProductRepository investmentProductRepository;

  @Override
  public List<InvestProductSummary> getListByPivot(OffsetDateTime pivot) {
    return investmentProductRepository.findAllByPivotOrderByIdDesc(pivot);
  }
}
