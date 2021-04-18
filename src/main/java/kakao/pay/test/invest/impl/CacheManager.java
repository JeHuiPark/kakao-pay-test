package kakao.pay.test.invest.impl;

import java.util.Optional;

/**
 * 캐시 관리자.
 * @param <V> 캐시 value
 * @param <K> 캐시 key
 */
public interface CacheManager<V, K extends CacheKeyProvider> {

  void purge(K key);

  void set(K key, V value);

  Optional<V> get(K key);
}
