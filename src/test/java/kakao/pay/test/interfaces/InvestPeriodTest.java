package kakao.pay.test.interfaces;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @see InvestPeriod
 */
@DisplayName("InvestPeriod")
class InvestPeriodTest {

  private abstract static class ConstructorTestContext {
    abstract OffsetDateTime givenStartedAt();
    abstract OffsetDateTime givenFinishedAt();

    InvestPeriod subject() {
      return new InvestPeriod(givenStartedAt(), givenFinishedAt());
    }
  }

  @Nested
  @DisplayName("생성자")
  class Describe_constructor {

    @Nested
    @DisplayName("시작일과 종료일이 같으면")
    class Context_case1 extends ConstructorTestContext {

      @Override
      OffsetDateTime givenStartedAt() {
        return OffsetDateTime.of(2021, 1, 1, 1, 0, 0, 0, ZoneOffset.UTC);
      }

      @Override
      OffsetDateTime givenFinishedAt() {
        return givenStartedAt();
      }

      @Test
      @DisplayName("IllegalArgumentException 예외가 발생합니다")
      void test1() {
        assertThrows(
            IllegalArgumentException.class,
            this::subject
        );
      }
    }

    @Nested
    @DisplayName("시작일이 종료일보다 늦으면")
    class Context_case2 extends ConstructorTestContext {

      @Override
      OffsetDateTime givenStartedAt() {
        return OffsetDateTime.of(2021, 1, 1, 1, 0, 0, 0, ZoneOffset.UTC);
      }

      @Override
      OffsetDateTime givenFinishedAt() {
        return givenStartedAt().minusDays(1);
      }

      @Test
      @DisplayName("IllegalArgumentException 예외가 발생합니다")
      void test2() {
        assertThrows(
            IllegalArgumentException.class,
            this::subject
        );
      }
    }

    @Nested
    @DisplayName("시작일이 종료일보다 빠르면")
    class Context_case3 extends ConstructorTestContext {

      @Override
      OffsetDateTime givenStartedAt() {
        return OffsetDateTime.of(2021, 1, 1, 1, 0, 0, 0, ZoneOffset.UTC);
      }

      @Override
      OffsetDateTime givenFinishedAt() {
        return givenStartedAt().plusDays(1);
      }

      @Test
      @DisplayName("정상 리턴합니다")
      void test3() {
        subject();
      }
    }
  }
}
