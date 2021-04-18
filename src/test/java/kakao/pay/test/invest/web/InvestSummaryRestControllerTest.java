package kakao.pay.test.invest.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductSummary;
import kakao.pay.test.invest.interfaces.InvestProductSummaryService;
import kakao.pay.test.invest.interfaces.InvestProductType;
import kakao.pay.test.web.MockMvcTestBased;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @see InvestSummaryRestController
 */
@DisplayName("InvestSummaryRestControllerTest")
class InvestSummaryRestControllerTest {

  @WebMvcTest(InvestSummaryRestController.class)
  private static class InvestingServiceTestContext extends MockMvcTestBased {

    @MockBean
    InvestProductSummaryService investProductSummaryService;

  }

  @Nested
  @DisplayName("전체 투자상품 조회 API")
  class Describe_getActivatedInvestList {

    @Nested
    @DisplayName("품절된 투자상품은")
    class Context_case1 extends InvestingServiceTestContext {

      @BeforeEach
      void setupContext() {
        when(investProductSummaryService.getListByPivot(any()))
            .thenReturn(List.of(new MockInvestProductSummary(10_000, 10_000)));
      }

      @Test
      @DisplayName("모집완료로 표기한다")
      void test3() throws Exception {
        mockMvc.perform(get("/invests/activated"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].status").value("FINISHED"));
      }
    }

    @Nested
    @DisplayName("품절되지 않은 투자상품은")
    class Context_case2 extends InvestingServiceTestContext {

      @BeforeEach
      void setupContext() {
        when(investProductSummaryService.getListByPivot(any()))
            .thenReturn(List.of(new MockInvestProductSummary(10_000, 5_000)));
      }

      @Test
      @DisplayName("모집중으로 표기한다")
      void test4() throws Exception {
        mockMvc.perform(get("/invests/activated"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].status").value("PROGRESSING"));
      }
    }
  }

  private static class MockInvestProductSummary implements InvestProductSummary {

    private final long totalInvestingAmount;
    private final long accumulatedInvestingAmount;

    MockInvestProductSummary(long totalInvestingAmount, long accumulatedInvestingAmount) {
      assert totalInvestingAmount >= accumulatedInvestingAmount : "context error";
      this.totalInvestingAmount = totalInvestingAmount;
      this.accumulatedInvestingAmount = accumulatedInvestingAmount;
    }

    @Override
    public long productId() {
      return 123;
    }

    @Override
    public String title() {
      return "테스트상품";
    }

    @Override
    public long totalInvestingAmount() {
      return totalInvestingAmount;
    }

    @Override
    public InvestPeriod investPeriod() {
      return new InvestPeriod(
          OffsetDateTime.now().minusDays(1),
          OffsetDateTime.now().plusDays(1)
      );
    }

    @Override
    public InvestProductType investProductType() {
      return null;
    }

    @Override
    public long investorCount() {
      return 10;
    }

    @Override
    public long accumulatedInvestingAmount() {
      return accumulatedInvestingAmount;
    }
  }
}
