package kakao.pay.test.invest.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 캐시 Key.
 *
 * @see CacheManager
 * @see InvestProductReceiptsCacheManager
 */
@Getter
@RequiredArgsConstructor
public class UserInvestReceiptsKey implements CacheKeyProvider {
  private final long userId;

  /**
   * 주어진 key를 이용하여 UserInvestReceiptsKey를 생성합니다.
   *
   * @throws IllegalArgumentException 지원하지 않는 형식일 경우
   */
  public static UserInvestReceiptsKey deserialize(String key) throws IllegalArgumentException {
    var userIdStr = key.replace("user_invest:", "");
    try {
      return new UserInvestReceiptsKey(Long.parseLong(userIdStr));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          String.format("지원하는 형식의 키가 아닙니다. input = '%s'", key));
    }
  }

  @Override
  public String key() {
    return "user_invest:" + userId;
  }
}
