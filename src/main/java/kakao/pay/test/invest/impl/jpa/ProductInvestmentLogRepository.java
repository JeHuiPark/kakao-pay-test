package kakao.pay.test.invest.impl.jpa;

import java.util.Optional;
import kakao.pay.test.invest.interfaces.ProductInvestor;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductInvestmentLogRepository extends JpaRepository<ProductInvestmentLog, Long> {

  Optional<ProductInvestmentLog> findByKey(String key);

  /**
   * 상품 투자자 정보를 기록 합니다.
   * @param productInvestor 기록 대상
   * @return 기록된 적이 없으면 true 를 리턴합니다.
   */
  default boolean logging(ProductInvestor productInvestor) {
    var productInvestorLog = new ProductInvestmentLog(productInvestor);
    if (findByKey(productInvestorLog.getKey()).isEmpty()) {
      saveAndFlush(productInvestorLog);
      return true;
    }
    return false;
  }
}
