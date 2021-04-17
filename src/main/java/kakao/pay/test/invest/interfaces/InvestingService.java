package kakao.pay.test.invest.interfaces;

import kakao.pay.test.invest.interfaces.exception.InvestingStateException;
import kakao.pay.test.invest.interfaces.exception.NotExistProductException;

/**
 * 투자 서비스.
 */
public interface InvestingService {

  /**
   * 주어진 투자 요청서를 이용하여 투자를 진행합니다.
   *
   * @param investingCommand 투자 요청서
   * @throws InvestingStateException 투자 요청서를 처리할 수 없는 상태일 경우
   * @throws NotExistProductException 투자 요청서와 일치하는 투자 상품이 없을 경우
   */
  void investing(InvestingCommand investingCommand) throws InvestingStateException,
      NotExistProductException;
}
