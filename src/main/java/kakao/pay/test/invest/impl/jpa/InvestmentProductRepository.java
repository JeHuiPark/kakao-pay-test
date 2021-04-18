package kakao.pay.test.invest.impl.jpa;

import java.time.OffsetDateTime;
import java.util.List;
import kakao.pay.test.invest.interfaces.InvestProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {

  /**
   * 투자요약정보(투자금액, 충 투자자)를 집계합니다.
   *
   * @param productId 상품 식별자
   * @param amount 투자금액
   */
  @Query(
      "update InvestmentProduct i "
          + "set i.accumulatedInvestingAmount = i.accumulatedInvestingAmount + :amount, "
          + "    i.investorCount = i.investorCount + 1 "
          + "where i.id = :productId ")
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void accumulateInvestmentAmountWithInvestor(@Param("productId") long productId,
                                              @Param("amount") long amount);

  /**
   * 투자요약정보(투자금액, 충 투자자)를 집계합니다.
   *
   * @param productId 상품 식별자
   * @param amount 투자금액
   */
  @Query(
      "update InvestmentProduct i "
          + "set i.accumulatedInvestingAmount = i.accumulatedInvestingAmount + :amount "
          + "where i.id = :productId ")
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void accumulateInvestmentAmount(@Param("productId") long productId,
                                  @Param("amount") long amount);

  @Query("from InvestmentProduct i "
      + "where i.startedAt <= :pivot "
      + "  and i.finishedAt >= :pivot "
      + "order by i.id desc")
  List<InvestProductSummary> findAllByPivotOrderByIdDesc(
      @Param("pivot") OffsetDateTime offsetDateTime);
}
