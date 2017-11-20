package io.projectreactor.demos.in.action.testing;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

/**
 * @author Simon Baslé
 */
@Service
public class Testing {

	//TODO how to test Flux returned by methods in this class?

	static final Set<String> NAMES = new LinkedHashSet<>(Arrays.asList(
			"Victor","Simon","Rick","Morty","Beth","Jerry","Summer"
	));

	public Flux<Integer> tenToZero() {
		return Flux.range(0, 11)
		           .map(i -> 10 - i);
	}

	public Flux<Integer> operateOnTenToZero(Function<Integer, Integer> operation) {
		return tenToZero().map(operation);
	}

	public Flux<String> namesPerSecond() {
		List<String> randomizedNames = new ArrayList<>(NAMES);
		Collections.reverse(randomizedNames);

		return Flux.fromIterable(randomizedNames)
				.delayElements(Duration.ofSeconds(1));
	}
}
