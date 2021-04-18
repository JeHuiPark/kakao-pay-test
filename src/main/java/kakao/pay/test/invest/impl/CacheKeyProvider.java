package kakao.pay.test.invest.impl;

/**
 * 캐시 키 제공자.
 */
public interface CacheKeyProvider {

  /**
   * 캐시 키를 리턴합니다.
   */
  String key();
}
