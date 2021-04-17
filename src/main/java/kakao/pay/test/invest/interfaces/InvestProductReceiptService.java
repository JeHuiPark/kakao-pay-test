package kakao.pay.test.invest.interfaces;

import java.util.List;

/**
 * 투자정보 조회 서비스.
 */
public interface InvestProductReceiptService {

  /**
   * userId와 일치하는 투자정보들을 리턴합니다.
   * @param userId 사용자 식별값
   * @return 투자정보 목록
   */
  List<InvestProductReceipt> findAllByUserId(long userId);
}
