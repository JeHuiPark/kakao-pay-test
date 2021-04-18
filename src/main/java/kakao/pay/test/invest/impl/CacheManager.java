package kakao.pay.test.invest.impl;

import java.util.Optional;

/**
 * 캐시 관리자.
 * @param <V> 캐시 value
 * @param <K> 캐시 key
 */
public interface CacheManager<V, K extends CacheKeyProvider> {

  /**
   * 주어진 key 와 일치하는 캐시 데이터를 제거합니다.
   */
  void purge(K key);

  /**
   * 캐시 데이터를 key 에 저장합니다. <br>
   */
  void set(K key, V value);

  /**
   * 주어진 key 와 일치하는 캐시 데이터를 조회합니다. <br>
   * 저장된 캐시가 없으면 empty 를 반환합니다.
   */
  Optional<V> get(K key);
}
