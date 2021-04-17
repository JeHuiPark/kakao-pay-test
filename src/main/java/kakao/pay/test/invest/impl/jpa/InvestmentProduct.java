package kakao.pay.test.invest.impl.jpa;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductSummary;
import kakao.pay.test.invest.interfaces.InvestProductType;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Table(name = "invest_product")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentProduct implements InvestProductSummary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "type", length = 20)
  private InvestProductType investProductType;

  @Column(name = "total_investing_amount")
  private long totalInvestingAmount;

  @Column(name = "investor_count")
  private long investorCount;

  @Column(name = "accumulated_investing_amount")
  private long accumulatedInvestingAmount;

  @Column(name = "started_at")
  private OffsetDateTime startedAt;

  @Column(name = "finished_at")
  private OffsetDateTime finishedAt;

  @Builder
  InvestmentProduct(@NonNull String title,
                    @NonNull InvestProductType investProductType,
                    @NonNull Long totalInvestingAmount,
                    @NonNull InvestPeriod investPeriod) {
    this.title = title;
    this.investProductType = investProductType;
    this.totalInvestingAmount = totalInvestingAmount;
    this.startedAt = investPeriod.getStartedAt();
    this.finishedAt = investPeriod.getFinishedAt();
  }

  @Override
  public long productId() {
    return id;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public long totalInvestingAmount() {
    return totalInvestingAmount;
  }

  @Override
  public InvestPeriod investPeriod() {
    return new InvestPeriod(startedAt, finishedAt);
  }

  @Override
  public InvestProductType investProductType() {
    return investProductType;
  }

  @Override
  public long investorCount() {
    return investorCount;
  }

  @Override
  public long accumulatedInvestingAmount() {
    return accumulatedInvestingAmount;
  }

  public boolean isOverflow() {
    return accumulatedInvestingAmount > totalInvestingAmount;
  }

  public InvestmentReceipt invest(InvestingCommand investingCommand) {
    return new InvestmentReceipt(this, investingCommand);
  }
}
