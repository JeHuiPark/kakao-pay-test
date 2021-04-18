package kakao.pay.test.invest.impl.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import kakao.pay.test.invest.interfaces.ProductInvestor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "invest_product_investment_log")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ProductInvestmentLog {

  @Id
  @Column(name = "id", length = 20)
  String key;

  ProductInvestmentLog(ProductInvestor productInvestor) {
    this.key = String.format("%d:%d", productInvestor.userId(), productInvestor.productId());
  }
}
