package kakao.pay.test.invest.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * @see InvestProductSummaryServiceImpl
 */
@DisplayName("InvestProductSummaryServiceImplTest")
class InvestProductSummaryServiceImplTest {

  @DataJpaTest
  @Import(InvestProductSummaryServiceImpl.class)
  private static class ListByPivotTestContext extends PreparedInvestmentProductTestContext {

    @Autowired
    private InvestProductSummaryServiceImpl investProductSummaryServiceImpl;

    @Override
    InvestPeriod preparedProductPeriod() {
      return new InvestPeriod(
          OffsetDateTime.of(2021, 1, 1, 1, 0, 0, 0, ZoneOffset.UTC),
          OffsetDateTime.of(2021, 1, 4, 1, 0, 0, 0, ZoneOffset.UTC));
    }

    @Override
    long preparedProductTotalInvestingAmount() {
      return 10_000;
    }

    List<InvestProductSummary> subject(OffsetDateTime given) {
      return investProductSummaryServiceImpl.getListByPivot(given);
    }
  }

  @Nested
  @DisplayName("getListByPivot")
  class Describe_getListByPivot {

    @Nested
    @DisplayName("기준시각을 기준으로 유효한 투자상품이 있으면")
    class Context_case1 extends ListByPivotTestContext {

      @ValueSource(
          strings = {
              "2021-01-01T10:00:00.000+09:00", // startedAt edge
              "2021-01-01T01:00:00.000+00:00", // startedAt edge
              "2021-01-02T01:00:00.000+00:00",
              "2021-01-03T01:00:00.000+00:00",
              "2021-01-04T10:00:00.000+09:00", // finishedAt edge
              "2021-01-04T01:00:00.000+00:00", // finishedAt edge
          }
      )
      @ParameterizedTest
      @DisplayName("목록으로 리턴한다")
      void test2(OffsetDateTime given) {
        var actual = subject(given);

        assertEquals(1, actual.size());
      }
    }

    @Nested
    @DisplayName("기준시각을 기준으로 유효한 투자상품이 없으면")
    class Context_case2 extends ListByPivotTestContext {

      @ValueSource(
          strings = {
              "2021-01-01T09:59:59.999+09:00",
              "2021-01-01T00:59:59.999+00:00",
              "2021-01-04T10:00:00.001+09:00",
              "2021-01-04T01:00:00.001+00:00",
          }
      )
      @ParameterizedTest
      @DisplayName("빈 목록을 리턴한다")
      void test3(OffsetDateTime given) {
        var actual = subject(given);

        assertTrue(actual.isEmpty());
      }
    }
  }
}
