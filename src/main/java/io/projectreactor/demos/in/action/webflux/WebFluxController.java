package io.projectreactor.demos.in.action.webflux;

import java.util.List;

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
//TODO make a rest controller mapping to root "/api"
public class WebFluxController {

	Testing service;

	public WebFluxController(Testing service) {
		this.service = service;
	}

	//TODO make endpoint "/numbers" with request param
	public List<Integer> numbers(String operation) {
		if (operation == null)
			//TODO default to tenToZero
			return null;

		switch (operation) {
			case "divide":
				//TODO divide 100 by i (and prevent division from failing?)
				return null;
			case "multiply":
				//TODO multiply 100 by i
				return null;
			default:
				throw new UnsupportedOperationException("Unknown operation \"" + operation + "\"," + " try \"divide\" or \"multiply\"");
		}
	}

	//TODO make endpoint "/numbers/{index}" with path variable
	public Integer number(int index) {
		return service.tenToZero()
				//TODO replace
				.blockLast()
				;
	}

	//TODO make endpoint "/names" that streams from service.namesPerSecond()
	public String names() {
		return service.namesPerSecond()
				.blockLast() //TODO fix
				;
	}

}
