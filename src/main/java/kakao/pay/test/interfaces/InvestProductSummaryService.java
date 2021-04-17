package kakao.pay.test.interfaces;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 투자 요약정보 서비스.
 */
public interface InvestProductSummaryService {

  /**
   * 중심 값을 기준으로 투자기간이 유효한 상품들의 목록을 리턴합니다.
   *
   * @param pivot 유효한 투자기간을 판단하는 중심 값
   * @return 투자상품 요약정보 목록
   */
  List<InvestProductSummary> getListByPivot(OffsetDateTime pivot);
}
