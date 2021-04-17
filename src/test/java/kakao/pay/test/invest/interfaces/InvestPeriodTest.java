package kakao.pay.test.invest.interfaces;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

  private abstract static class ContainsTestContext {
    abstract OffsetDateTime startedAt();
    abstract OffsetDateTime finishedAt();

    boolean subject(OffsetDateTime given) {
      return new InvestPeriod(startedAt(), finishedAt()).contains(given);
    }
  }

  @Nested
  @DisplayName("contains")
  class Describe_contains {

    @Nested
    @DisplayName("주어진 값이 시작일과 종료일 사이가 아니면")
    class Context_case1 extends ContainsTestContext {

      @Override
      OffsetDateTime startedAt() {
        return OffsetDateTime.of(2021, 1, 1, 8, 0, 0, 0, ZoneOffset.UTC);
      }

      @Override
      OffsetDateTime finishedAt() {
        return OffsetDateTime.of(2021, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC);
      }

      @ValueSource(strings = {
          "2021-01-01T07:59:59.999Z", // 모집일 이전
          "2021-02-01T00:00:00.001Z", // 모집일 이후
      })
      @ParameterizedTest
      @DisplayName("false 를 리턴한다")
      void test4(OffsetDateTime given) {
        var actual = subject(given);

        assertFalse(actual);
      }
    }

    @Nested
    @DisplayName("주어진 값이 시작일과 종료일 사이면")
    class Context_case2 extends ContainsTestContext {

      @Override
      OffsetDateTime startedAt() {
        return OffsetDateTime.of(2021, 1, 1, 8, 0, 0, 0, ZoneOffset.UTC);
      }

      @Override
      OffsetDateTime finishedAt() {
        return OffsetDateTime.of(2021, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC);
      }

      @ValueSource(strings = {
          "2021-01-01T08:00:00.000Z", // 모집 시작일 엣지 케이스
          "2021-01-01T13:00:00.000Z",
          "2021-02-01T00:00:00.000Z", // 모집 종료일 엣지 케이스
      })
      @ParameterizedTest
      @DisplayName("true를 리턴한다")
      void test5(OffsetDateTime given) {
        var actual = subject(given);

        assertTrue(actual);
      }
    }
  }
}
