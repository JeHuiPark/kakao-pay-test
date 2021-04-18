package kakao.pay.test.invest.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kakao.pay.test.invest.interfaces.InvestingService;
import kakao.pay.test.invest.interfaces.exception.InvestingPeriodException;
import kakao.pay.test.invest.interfaces.exception.NotExistProductException;
import kakao.pay.test.invest.interfaces.exception.SoldOutException;
import kakao.pay.test.invest.interfaces.exception.TotalAmountExceedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @see InvestRestController
 */
@DisplayName("InvestRestControllerTest")
class InvestRestControllerTest {

  @WebMvcTest(InvestRestController.class)
  @AutoConfigureMockMvc
  private static class InvestingServiceTestContext {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InvestingService investingService;
  }

  @Nested
  @DisplayName("투자하기 API")
  class Describe_investing {

    @Nested
    @DisplayName("정상처리 되면")
    class Context_case1 extends InvestingServiceTestContext {

      @Test
      @DisplayName("204 응답을 리턴한다")
      void test3() throws Exception {
        mockMvc.perform(
            post("/investing/{productId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1000}")
                .header("X-USER-ID", "1")
        )
            .andDo(print())
            .andExpect(status().isNoContent());
      }
    }

    @Nested
    @DisplayName("투자기간이 아니면")
    class Context_case2 extends InvestingServiceTestContext {

      @BeforeEach
      void setupContext() {
        doThrow(new InvestingPeriodException())
            .when(investingService).investing(any());
      }

      @Test
      @DisplayName("409 응답을 리턴한다")
      void test4() throws Exception {
        mockMvc.perform(
            post("/investing/{productId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1000}")
                .header("X-USER-ID", "1")
        )
            .andDo(print())
            .andExpect(status().isConflict());
      }
    }

    @Nested
    @DisplayName("일치하는 상품정보가 없으면")
    class Context_case3 extends InvestingServiceTestContext {

      @BeforeEach
      void setupContext() {
        doThrow(new NotExistProductException())
            .when(investingService).investing(any());
      }

      @Test
      @DisplayName("400 응답을 리턴한다")
      void test5() throws Exception {
        mockMvc.perform(
            post("/investing/{productId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1000}")
                .header("X-USER-ID", "1")
        )
            .andDo(print())
            .andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("투자상품이 매진되면")
    class Context_case4 extends InvestingServiceTestContext {

      @BeforeEach
      void setupContext() {
        doThrow(new SoldOutException())
            .when(investingService).investing(any());
      }

      @Test
      @DisplayName("409 응답을 리턴한다")
      void test6() throws Exception {
        mockMvc.perform(
            post("/investing/{productId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1000}")
                .header("X-USER-ID", "1")
        )
            .andDo(print())
            .andExpect(status().isConflict());
      }
    }

    @Nested
    @DisplayName("투자상품의 총 모집금액을 넘어서면")
    class Context_case5 extends InvestingServiceTestContext {

      @BeforeEach
      void setupContext() {
        doThrow(new TotalAmountExceedException())
            .when(investingService).investing(any());
      }

      @Test
      @DisplayName("409 응답을 리턴한다")
      void test7() throws Exception {
        mockMvc.perform(
            post("/investing/{productId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1000}")
                .header("X-USER-ID", "1")
        )
            .andDo(print())
            .andExpect(status().isConflict());
      }
    }

    @Nested
    @DisplayName("최소 투자금액을 만족하지 못 하면")
    class Context_case6 extends InvestingServiceTestContext {

      @Test
      @DisplayName("400 응답을 리턴한다")
      void test8() throws Exception {
        mockMvc.perform(
            post("/investing/{productId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":500}")
                .header("X-USER-ID", "1")
        )
            .andDo(print())
            .andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("사용자 정보가 없으면")
    class Context_case7 extends InvestingServiceTestContext {

      @Test
      @DisplayName("400 응답을 리턴한다")
      void test9() throws Exception {
        mockMvc.perform(
            post("/investing/{productId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1000}")
        )
            .andDo(print())
            .andExpect(status().isBadRequest());
      }
    }
  }
}
