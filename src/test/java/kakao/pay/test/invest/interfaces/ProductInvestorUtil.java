package kakao.pay.test.invest.interfaces;

public class ProductInvestorUtil {

  public static ProductInvestor build(long productId, long userId) {
    return new ProductInvestor() {
      @Override
      public long userId() {
        return userId;
      }

      @Override
      public long productId() {
        return productId;
      }
    };
  }
}
