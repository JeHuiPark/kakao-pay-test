package kakao.pay.test.invest.impl.redis;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.convert.RedisCustomConversions;

@Configuration
class RedisConfig {

  @Bean
  public RedisCustomConversions redisCustomConversions() {
    return new RedisCustomConversions(
        List.of(new OffsetDateTimeSerializer(), new OffsetDateTimeDeserializer())
    );
  }

  private static class OffsetDateTimeSerializer implements Converter<OffsetDateTime, byte[]> {

    @Override
    public byte[] convert(OffsetDateTime source) {
      return source.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME).getBytes();
    }
  }

  private static class OffsetDateTimeDeserializer implements Converter<byte[], OffsetDateTime> {

    @Override
    public OffsetDateTime convert(byte[] source) {
      return OffsetDateTime.parse(new String(source), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
  }
}
