package kakao.pay.test.invest.impl.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import kakao.pay.test.invest.impl.UserInvestReceipts;
import kakao.pay.test.invest.impl.UserInvestReceiptsKey;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import kakao.pay.test.invest.interfaces.InvestProductType;
import kakao.pay.test.invest.interfaces.ProductInvestor;
import kakao.pay.test.invest.interfaces.ProductInvestorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @see InvestProductReceiptCacheManagerImpl
 */
@DisplayName("InvestProductReceiptCacheManagerImplTest")
class InvestProductReceiptCacheManagerImplTest {

  @SpringBootTest
  private abstract static class TestBased {

    @Autowired
    InvestProductReceiptCacheManagerImpl investProductReceiptCacheManager;

    @Autowired
    InvestProductReceiptsRepository investProductReceiptsRepository;

    UserInvestReceiptsCache preparedCache;

    @BeforeEach
    private void setupContext() {
      var userId = 1;
      preparedCache = new UserInvestReceiptsCache(
          new UserInvestReceiptsKey(userId),
          new UserInvestReceipts(
              List.of(
                  new InternalInvestProductReceipt(
                      ProductInvestorUtil.build(1, userId),
                      1_000
                  )
              )
          )
      );
      investProductReceiptsRepository.save(preparedCache);
    }
  }

  private static class PurgeTestContext extends TestBased {

    Optional<UserInvestReceiptsCache> subject(UserInvestReceiptsKey givenKey) {
      investProductReceiptCacheManager.purge(givenKey);
      return investProductReceiptsRepository.findById(givenKey.key());
    }
  }

  @Nested
  @DisplayName("purge")
  class Describe_purge {

    @Nested
    @DisplayName("key가 주어지면")
    class Context_case1 extends PurgeTestContext {

      @Test
      @DisplayName("키와 일치하는 캐시 데이터를 제거합니다")
      void test4() {
        var actual = subject(preparedCache.getKey());

        assertTrue(actual.isEmpty());
      }
    }
  }

  private static class SetTestContext extends TestBased {

    UserInvestReceiptsCache subject(UserInvestReceiptsKey givenKey, UserInvestReceipts givenValue) {
      investProductReceiptCacheManager.set(givenKey, givenValue);
      return investProductReceiptsRepository.findById(givenKey.key()).orElseThrow();
    }
  }

  @Nested
  @DisplayName("set")
  class Describe_set {

    @Nested
    @DisplayName("key와 value가 주어지면")
    class Context_case1 extends SetTestContext {

      InternalInvestProductReceipt mockData;

      @BeforeEach
      void setupMockData() {
        mockData = new InternalInvestProductReceipt(
            ProductInvestorUtil.build(999, preparedCache.getKey().getUserId()),
            1_000_000
        );
      }

      UserInvestReceiptsKey givenKey() {
        return preparedCache.getKey();
      }

      UserInvestReceipts givenValue() {
        return new UserInvestReceipts(List.of(mockData));
      }

      @Test
      @DisplayName("key에 value를 저장합니다")
      void test3() {
        var actual = subject(givenKey(), givenValue())
            .getValue().getGetReceipts().get(0);

        assertEquals(mockData.productId(), actual.productId());
        assertEquals(mockData.userId(), actual.userId());
        assertEquals(mockData.investingAmount(), actual.investingAmount());
        assertEquals(mockData.investingAt(), actual.investingAt());
        assertEquals(mockData.title(), actual.title());
        assertEquals(mockData.totalInvestingAmount(), actual.totalInvestingAmount());
        assertEquals(mockData.investPeriod().getStartedAt(), actual.investPeriod().getStartedAt());
        assertEquals(mockData.investPeriod().getFinishedAt(), actual.investPeriod().getFinishedAt());
        assertEquals(mockData.investProductType(), actual.investProductType());
      }
    }
  }

  private static class GetTestContext extends TestBased {

    Optional<UserInvestReceipts> subject(UserInvestReceiptsKey given) {
      return investProductReceiptCacheManager.get(given);
    }
  }

  @Nested
  @DisplayName("get")
  class Describe_get {

    @Nested
    @DisplayName("주어진 key와 일치하는 캐시 데이터가 존재하면")
    class Context_case1 extends GetTestContext {

      @Test
      @DisplayName("캐시 데이터를 리턴합니다")
      void test5() {
        var expected = preparedCache.getValue().getGetReceipts().get(0);

        var actual = subject(preparedCache.getKey()).orElseThrow().getGetReceipts().get(0);

        assertEquals(expected.productId(), actual.productId());
        assertEquals(expected.userId(), actual.userId());
        assertEquals(expected.investingAmount(), actual.investingAmount());
        assertEquals(expected.investingAt(), actual.investingAt());
        assertEquals(expected.title(), actual.title());
        assertEquals(expected.totalInvestingAmount(), actual.totalInvestingAmount());
        assertEquals(expected.investPeriod().getStartedAt(), actual.investPeriod().getStartedAt());
        assertEquals(expected.investPeriod().getFinishedAt(), actual.investPeriod().getFinishedAt());
        assertEquals(expected.investProductType(), actual.investProductType());
      }
    }

    @Nested
    @DisplayName("주어진 key와 일치하는 캐시 데이터가 없으면")
    class Context_case2 extends GetTestContext {

      UserInvestReceiptsKey given() {
        return new UserInvestReceiptsKey(Long.MAX_VALUE);
      }

      @Test
      @DisplayName("빈 값을 리턴합니다")
      void test6() {
        var actual = subject(given());

        assertTrue(actual.isEmpty());
      }
    }
  }

  private static class InternalInvestProductReceipt implements InvestProductReceipt {

    private final ProductInvestor productInvestor;
    private final long investingAmount;
    private final InvestPeriod investPeriod;
    private final OffsetDateTime investingAt;

    InternalInvestProductReceipt(ProductInvestor productInvestor, long investingAmount) {
      this.productInvestor = productInvestor;
      this.investingAmount = investingAmount;
      this.investPeriod = new InvestPeriod(
          OffsetDateTime.now().minusDays(1),
          OffsetDateTime.now().plusDays(1)
      );
      this.investingAt = OffsetDateTime.now();
    }

    @Override
    public long investingAmount() {
      return investingAmount;
    }

    @Override
    public OffsetDateTime investingAt() {
      return investingAt;
    }

    @Override
    public long userId() {
      return productInvestor.userId();
    }

    @Override
    public long productId() {
      return productInvestor.productId();
    }

    @Override
    public String title() {
      return "테스트상품";
    }

    @Override
    public long totalInvestingAmount() {
      return 1_000_000;
    }

    @Override
    public InvestPeriod investPeriod() {
      return investPeriod;
    }

    @Override
    public InvestProductType investProductType() {
      return InvestProductType.CREDIT;
    }
  }
}
