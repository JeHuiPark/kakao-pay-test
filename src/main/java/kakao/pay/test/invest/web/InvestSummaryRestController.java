package kakao.pay.test.invest.web;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kakao.pay.test.invest.interfaces.InvestProductSummary;
import kakao.pay.test.invest.interfaces.InvestProductSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class InvestSummaryRestController {

  private final InvestProductSummaryService investProductSummaryService;

  @GetMapping("/invests/activated")
  public List<SimpleInvestSummary> getActivatedInvestList() {
    return investProductSummaryService.getListByPivot(OffsetDateTime.now())
        .stream()
        .map(SimpleInvestSummary::new)
        .collect(Collectors.toList());
  }

  @RequiredArgsConstructor
  private static class SimpleInvestSummary {

    private final InvestProductSummary investProductSummary;

    public long getProductId() {
      return investProductSummary.productId();
    }

    public String getTitle() {
      return investProductSummary.title();
    }

    public long getAccumulatedInvestingAmount() {
      return investProductSummary.accumulatedInvestingAmount();
    }

    public long getInvestorCount() {
      return investProductSummary.investorCount();
    }

    public String getStatus() {
      return investProductSummary.isSoldOut()
          ? "FINISHED"
          : "PROGRESSING";
    }

    public OffsetDateTime getStartedAt() {
      return investProductSummary.investPeriod().getStartedAt();
    }

    public OffsetDateTime getFinishedAt() {
      return investProductSummary.investPeriod().getFinishedAt();
    }
  }
}
