package kakao.pay.test.invest.impl.jpa;

import java.util.Optional;
import kakao.pay.test.invest.impl.ProductInvestorLogger;
import kakao.pay.test.invest.interfaces.ProductInvestor;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductInvestmentLogRepository extends JpaRepository<ProductInvestmentLog, Long>,
    ProductInvestorLogger {

  Optional<ProductInvestmentLog> findByKey(String key);

  @Override
  default boolean logging(ProductInvestor productInvestor) {
    var productInvestorLog = new ProductInvestmentLog(productInvestor);
    if (findByKey(productInvestorLog.getKey()).isEmpty()) {
      saveAndFlush(productInvestorLog);
      return true;
    }
    return false;
  }
}
