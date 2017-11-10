package io.projectreactor.demos.in.action.webclient;

import java.util.List;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Simon Baslé
 */
@RestController
public class ClientExample {

	private WebClient swapi = WebClient.create("http://swapi.co/api/");

	@GetMapping(value = "/api/starwars/{charId}"
//			, produces = MediaType.TEXT_EVENT_STREAM_VALUE
	)
	public Flux<String> foo(@PathVariable int charId) {
		return swapi.get()
		            .uri("/people/{id}", charId)
		            .retrieve()
		            .bodyToMono(Map.class)
		            .flatMapMany(json -> {
		            	@SuppressWarnings("unchecked")
			            Iterable<String> movieUris = (Iterable<String>) json.get("films");
			            return Flux.fromIterable(movieUris)
			                       .flatMap(url -> swapi.get().uri(url).retrieve().bodyToMono(Map.class))
			                       .map(map -> map.get("title") + "\n")
			                       .startWith("Character " + json.get("name") + " appears in movies:\n")
			                       .concatWith(Mono.just("\nand that's it!"));
		            });
	}


}
