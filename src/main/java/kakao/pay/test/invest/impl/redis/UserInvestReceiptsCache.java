package kakao.pay.test.invest.impl.redis;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kakao.pay.test.invest.impl.UserInvestReceipts;
import kakao.pay.test.invest.impl.UserInvestReceiptsKey;
import kakao.pay.test.invest.interfaces.InvestPeriod;
import kakao.pay.test.invest.interfaces.InvestProductReceipt;
import kakao.pay.test.invest.interfaces.InvestProductType;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("invest_receipt")
@NoArgsConstructor
class UserInvestReceiptsCache {

  @Id
  private String key;

  private List<InvestProductReceipt> investProductReceipts;

  @TimeToLive
  private long ttl;

  UserInvestReceiptsCache(UserInvestReceiptsKey key, UserInvestReceipts investProductReceipts) {
    this.key = key.key();
    this.investProductReceipts = investProductReceipts.getGetReceipts()
        .stream()
        .map(RedisSchema::new)
        .collect(Collectors.toList());
    this.ttl = 600;
  }

  public UserInvestReceiptsKey getKey() {
    return UserInvestReceiptsKey.deserialize(key);
  }

  public UserInvestReceipts getValue() {
    return new UserInvestReceipts(investProductReceipts);
  }


  private static class RedisSchema implements InvestProductReceipt {

    private long userId;
    private long productId;
    private String title;
    private long totalInvestingAmount;
    private OffsetDateTime startedAt;
    private OffsetDateTime finishedAt;
    private InvestProductType investProductType;
    private long investingAmount;
    private OffsetDateTime investingAt;

    RedisSchema() {
    }

    RedisSchema(InvestProductReceipt in) {
      this.userId = in.userId();
      this.productId = in.productId();
      this.title = in.title();
      this.totalInvestingAmount = in.totalInvestingAmount();
      this.startedAt = in.investPeriod().getStartedAt();
      this.finishedAt = in.investPeriod().getFinishedAt();
      this.investProductType = in.investProductType();
      this.investingAmount = in.investingAmount();
      this.investingAt = in.investingAt();
    }

    @Override
    public long userId() {
      return userId;
    }

    @Override
    public long productId() {
      return productId;
    }

    @Override
    public String title() {
      return title;
    }

    @Override
    public long totalInvestingAmount() {
      return totalInvestingAmount;
    }

    @Override
    public InvestPeriod investPeriod() {
      return new InvestPeriod(
          startedAt,
          finishedAt
      );
    }

    @Override
    public InvestProductType investProductType() {
      return investProductType;
    }

    @Override
    public long investingAmount() {
      return investingAmount;
    }

    @Override
    public OffsetDateTime investingAt() {
      return investingAt;
    }
  }
}
