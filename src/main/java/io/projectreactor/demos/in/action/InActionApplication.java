package io.projectreactor.demos.in.action;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InActionApplication {

	public static void main(String[] args) {
		SpringApplication.run(InActionApplication.class, args);
	}

	@Bean
	public static Function<Integer, Integer> divideTenBy() {
		return i -> 10 / i;
	}
}
