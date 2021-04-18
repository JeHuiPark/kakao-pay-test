package kakao.pay.test.invest.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import kakao.pay.test.invest.interfaces.InvestingCommand;
import kakao.pay.test.invest.interfaces.InvestingService;
import kakao.pay.test.invest.interfaces.ProductInvestor;
import kakao.pay.test.invest.interfaces.exception.InvestingPeriodException;
import kakao.pay.test.invest.interfaces.exception.NotExistProductException;
import kakao.pay.test.invest.interfaces.exception.SoldOutException;
import kakao.pay.test.invest.interfaces.exception.TotalAmountExceedException;
import kakao.pay.test.web.ErrorResponse;
import kakao.pay.test.web.UserId;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
class InvestRestController {

  private final InvestingService investingService;

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PostMapping("/investing/{productId}")
  public void investing(@UserId long userId,
                        @PathVariable("productId") long productId,
                        @RequestBody @Valid InvestingRequest investingRequest) {
    investingService.investing(
        InvestingCommand.builder()
            .productInvestor(
                InternalProductInvestor.builder()
                    .userId(userId)
                    .productId(productId)
                    .build())
            .amount(investingRequest.amount)
            .build()
    );
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NotExistProductException.class)
  ErrorResponse handleNotExistProductException(NotExistProductException e) {
    return ErrorResponse.builder()
        .message("일치하는 상품이 없습니다")
        .build();
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(InvestingPeriodException.class)
  ErrorResponse handleInvestingPeriodException(InvestingPeriodException e) {
    return ErrorResponse.builder()
        .message("투자 기간이 아닙니다")
        .build();
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(SoldOutException.class)
  ErrorResponse handleSoldOutException(SoldOutException e) {
    return ErrorResponse.builder()
        .message("투자상품이 모집 종료됐습니다.")
        .build();
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(TotalAmountExceedException.class)
  ErrorResponse handleTotalAmountExceedException(TotalAmountExceedException e) {
    return ErrorResponse.builder()
        .message("투자상품이 모집 종료됐습니다.")
        .build();
  }



  private static class InvestingRequest {

    @Min(
        message = "투자 금액은 최소 1000원입니다.",
        value = 1000
    )
    private final long amount;

    @JsonCreator
    InvestingRequest(@JsonProperty("amount") long amount) {
      this.amount = amount;
    }
  }

  @Builder
  @RequiredArgsConstructor
  private static class InternalProductInvestor implements ProductInvestor {

    private final long userId;
    private final long productId;

    @Override
    public long userId() {
      return userId;
    }

    @Override
    public long productId() {
      return productId;
    }
  }
}
