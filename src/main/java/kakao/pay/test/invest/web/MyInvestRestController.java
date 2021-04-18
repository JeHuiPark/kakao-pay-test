package kakao.pay.test.invest.web;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import kakao.pay.test.invest.interfaces.InvestProductReceiptService;
import kakao.pay.test.web.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class MyInvestRestController {

  private final InvestProductReceiptService investProductReceiptService;

  @GetMapping("/invest/my-list")
  public List<SimpleInvestmentReceipt> myInvestmentList(@UserId long userId) {
    return investProductReceiptService.findAllByUserId(userId)
        .stream()
        .map(SimpleInvestmentReceipt::new)
        .collect(Collectors.toList());
  }

  @RequiredArgsConstructor
  private static class SimpleInvestmentReceipt {
    private final InvestProductReceipt investProductReceipt;

    public long getProductId() {
      return investProductReceipt.productId();
    }

    public String getTitle() {
      return investProductReceipt.title();
    }

    public long getTotalInvestingAmount() {
      return investProductReceipt.totalInvestingAmount();
    }

    public long myInvestingAmount() {
      return investProductReceipt.investingAmount();
    }

    public OffsetDateTime getInvestingAt() {
      return investProductReceipt.investingAt();
    }
  }
}
