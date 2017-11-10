package io.projectreactor.demos.in.action.webflux;

import io.projectreactor.demos.in.action.testing.Testing;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Simon Baslé
 */
@RestController
@RequestMapping("/api")
public class WebFluxController {

	Testing service;

	public WebFluxController(Testing service) {
		this.service = service;
	}

	@GetMapping("/numbers")
	public Flux<Integer> numbers(@RequestParam(required = false) String operation) {
		if (operation == null)
			return service.tenToZero();

		switch (operation) {
			case "divide":
				return service.tenToZero()
				              .filter(i -> i != 0)
				              .map(i -> 100 / i);
			case "multiply":
				return service.operateOnTenToZero(i -> 100 * i);
			default:
				throw new UnsupportedOperationException("Unknown operation \"" + operation + "\"," + " try \"divide\" or \"multiply\"");
		}
	}

	@GetMapping("/numbers/{index}")
	public Mono<Integer> number(@PathVariable int index) {
		return service.tenToZero()
		              .elementAt(index);
	}

	@GetMapping(value = "/names", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> names() {
		return service.namesPerSecond();
	}

}
