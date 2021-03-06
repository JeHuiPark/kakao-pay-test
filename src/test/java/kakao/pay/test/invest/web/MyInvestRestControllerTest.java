package kakao.pay.test.invest.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import kakao.pay.test.invest.interfaces.InvestProductReceiptService;
import kakao.pay.test.invest.interfaces.InvestProductType;
import kakao.pay.test.web.MockMvcTestBased;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

/**
 * @see MyInvestRestController
 */
@DisplayName("MyInvestRestControllerTest")
class MyInvestRestControllerTest {

  @AutoConfigureRestDocs
  @WebMvcTest(MyInvestRestController.class)
  private static class MyInvestmentListTestContext extends MockMvcTestBased {

    @MockBean
    InvestProductReceiptService investProductReceiptService;

    RestDocumentationResultHandler resultHandler() {
      return document("invest/my-list",
          responseFields(
              fieldWithPath("[]").type(JsonFieldType.ARRAY).description("투자정보 목록")
          ).andWithPrefix("[].",
              fieldWithPath("product_id").type(JsonFieldType.NUMBER).description("상품ID"),
              fieldWithPath("title").type(JsonFieldType.STRING).description("상품제목"),
              fieldWithPath("total_investing_amount").type(JsonFieldType.NUMBER).description("총 모집금액"),
              fieldWithPath("my_investing_amount").type(JsonFieldType.NUMBER).description("나의 투자금액"),
              fieldWithPath("investing_at").type(JsonFieldType.STRING).description("투자일시")
          )
      );
    }
  }
  
  @Nested
  @DisplayName("나의 투자상품 조회 API")
  class Describe_myInvestmentList extends MyInvestmentListTestContext {

    @Nested
    @DisplayName("사용자 정보가 없으면")
    class Context_case1 {

      @Test
      @DisplayName("400 응답을 리턴한다")
      void test3() throws Exception {
        mockMvc.perform(get("/invest/my-list"))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("정상처리 되면")
    class Context_case2 {

      @BeforeEach
      void setupContext() {
        when(investProductReceiptService.findAllByUserId(anyLong()))
            .thenReturn(List.of(new MockInvestProductReceipt()));
      }

      @Test
      @DisplayName("200 응답을 리턴한다")
      void test2() throws Exception {
        mockMvc.perform(
            get("/invest/my-list")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", "1")
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(resultHandler());
      }
    }
  }

  private static class MockInvestProductReceipt implements InvestProductReceipt {

    @Override
    public long userId() {
      return 0;
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
      return 10_000;
    }

    @Override
    public InvestPeriod investPeriod() {
      return null;
    }

    @Override
    public InvestProductType investProductType() {
      return null;
    }

    @Override
    public long investingAmount() {
      return 1_000;
    }

    @Override
    public OffsetDateTime investingAt() {
      return OffsetDateTime.now(ZoneOffset.UTC);
    }
  }
}
