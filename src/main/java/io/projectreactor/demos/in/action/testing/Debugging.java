package io.projectreactor.demos.in.action.testing;

import io.projectreactor.demos.in.action.InActionApplication;
import reactor.core.publisher.Flux;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Simon Baslé
 */
@Service
public class Debugging {

	private final Flux<Integer> source;

	public Debugging(Flux<Integer> source) {
		this.source = source;
	}

	public Flux<Integer> numbers() {
		return source;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(InActionApplication.class, args);

		ctx.getBean(Debugging.class)
		   .numbers()
		   .subscribe(System.out::println, e -> {
			   e.printStackTrace(System.out);
			   SpringApplication.exit(ctx);
		   });
	}
}
