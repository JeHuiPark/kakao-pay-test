package kakao.pay.test.interfaces;

import java.time.OffsetDateTime;
import lombok.Getter;

/**
 * 투자 모집기간.
 */
@Getter
public class InvestPeriod {

  private final OffsetDateTime startedAt;
  private final OffsetDateTime finishedAt;

  /**
   * 생성자.
   *
   * @param startedAt 투자모집시작일시
   * @param finishedAt 투자모집종료일시
   * @throws IllegalArgumentException 투자모집시작일시가 투자모집종료일시보다 이전 시간이 아닐경우
  */
  public InvestPeriod(OffsetDateTime startedAt, OffsetDateTime finishedAt)
      throws IllegalArgumentException {
    if (!finishedAt.isAfter(startedAt)) {
      throw new IllegalArgumentException(
          "투자모집시작일시는 투자모집종료일시보다 이전 날짜여야 합니다.");
    }
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }
}
