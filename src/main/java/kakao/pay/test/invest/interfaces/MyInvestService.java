package kakao.pay.test.invest.interfaces;

import java.util.List;

/**
 * 나의 투자정보 서비스.
 */
public interface MyInvestService {

  /**
   * 나의 투자상품목록을 리턴합니다.
   * @param userId 사용자 식별값
   * @return 나의 투자상품 목록
   */
  List<MyInvestProduct> myInvestList(long userId);
}
