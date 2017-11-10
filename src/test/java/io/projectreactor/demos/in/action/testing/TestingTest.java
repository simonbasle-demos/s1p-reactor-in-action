package io.projectreactor.demos.in.action.testing;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

import org.junit.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Simon Baslé
 */
public class TestingTest {

	private Testing test = new Testing();

	@Test
	public void tenToZero() {
		StepVerifier.create(test.tenToZero())
		            .expectNext(10)
		            .expectNext(9, 8, 7, 6, 5)
		            .expectNextCount(4)
		            .expectNext(0)
		            .verifyComplete();
	}

	@Test
	public void multiplyByTenToZero() {
		StepVerifier.create(test.operateOnTenToZero(i -> 10 * i))
		            .expectSubscription()
		            .expectNextCount(5)
		            .assertNext(v -> assertThat(v).isEqualTo(50))
		            .expectNext(40, 30, 20, 10, 0)
		            .verifyComplete();
	}

	@Test
	public void divideByTenToZeroErrors() {
		StepVerifier.create(test.operateOnTenToZero(i -> 10 / i))
		            .expectNext(1)
		            .expectNextCount(4)
		            .expectNext(2)
		            .expectNextCount(2)
		            .expectNext(5, 10)
		            .verifyErrorSatisfies(e -> assertThat(e).isInstanceOf(ArithmeticException.class)
		                                                    .hasMessage("/ by zero"));
	}

	@Test
	public void namesPerSecond() {
		StepVerifier.create(test.namesPerSecond()
		                        .doOnNext(System.out::println))
		            .recordWith(ArrayList::new)
		            .thenConsumeWhile(Objects::nonNull)
		            .consumeRecordedWith(l -> assertThat(Testing.NAMES).containsAll(l))
		            .verifyComplete();
	}

	@Test
	public void namesPerSecondWithoutTime() {
		StepVerifier.withVirtualTime(() -> test.namesPerSecond()
		                                       .doOnNext(System.out::println))
		            .expectSubscription()
		            .expectNoEvent(Duration.ofSeconds(1))
		            .expectNext("Summer")
		            .thenAwait(Duration.ofSeconds(2))
		            .expectNextCount(2)
		            .thenAwait(Duration.ofMinutes(1))
		            .expectNext("Morty", "Rick", "Simon", "Victor")
		            .verifyComplete();
	}
}