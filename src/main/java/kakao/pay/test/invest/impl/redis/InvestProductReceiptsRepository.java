package kakao.pay.test.invest.impl.redis;

import org.springframework.data.repository.CrudRepository;

public interface InvestProductReceiptsRepository extends CrudRepository<UserInvestReceiptsCache, String> {
}
