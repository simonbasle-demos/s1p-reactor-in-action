package io.projectreactor.demos.in.action.testing.support;

import java.util.function.Function;

import reactor.core.publisher.Flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * @author Simon Baslé
 */
@Component
public class FluxSource {

	private Function<Integer, Integer> operation;

	public FluxSource(Function<Integer, Integer> operation) {
		this.operation = operation;
	}

	@Conditional(Trigger1.class)
	@Bean
	public Flux<Integer> flux1() {
		return Flux.just(1, 2, 0, 3, 4)
		           .map(operation)
		           .checkpoint("flux1", true);
	}

	@Conditional(Trigger2.class)
	@Bean
	public Flux<Integer> flux2() {
		return Flux.range(1, 10)
		           .map(i -> 10 - i)
		           .map(operation)
		           .checkpoint("flux2");
	}
}
