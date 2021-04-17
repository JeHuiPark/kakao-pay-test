package kakao.pay.test.invest.impl.jpa;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import kakao.pay.test.invest.interfaces.InvestProductType;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Table(name = "invest_receipt")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentReceipt implements InvestProductReceipt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private InvestmentProduct investmentProduct;

  @Column(name = "user_id")
  private long userId;

  @Column(name = "second_key")
  private String secondKey;

  @Column(name = "amount")
  private long amount;

  @Column(name = "investing_at")
  private OffsetDateTime investingAt;

  InvestmentReceipt(InvestmentProduct investmentProduct,
                    InvestingCommand investingCommand) {
    this.investmentProduct = investmentProduct;
    this.userId = investingCommand.userId();
    this.amount = investingCommand.amount();
    this.investingAt = OffsetDateTime.now(ZoneOffset.UTC);
  }

  @Override
  public long userId() {
    return userId;
  }

  @Override
  public long productId() {
    return investmentProduct.productId();
  }

  @Override
  public String title() {
    return investmentProduct.title();
  }

  @Override
  public long totalInvestingAmount() {
    return investmentProduct.totalInvestingAmount();
  }

  @Override
  public InvestPeriod investPeriod() {
    return investmentProduct.investPeriod();
  }

  @Override
  public InvestProductType investProductType() {
    return investmentProduct.investProductType();
  }

  @Override
  public long investingAmount() {
    return amount;
  }

  @Override
  public OffsetDateTime investingAt() {
    return investingAt;
  }
}
