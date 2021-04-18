package kakao.pay.test.invest.impl.jpa;

import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvestmentReceiptRepository extends JpaRepository<InvestmentReceipt, Long> {

  @Query("from InvestmentReceipt ir join fetch ir.investmentProduct "
      + "where ir.userId = :userId "
      + "order by ir.investingAt desc")
  Stream<InvestmentReceipt> findAllByUserIdOrderByInvestingAtDesc(@Param("userId") long userId);
}
