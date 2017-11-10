package io.projectreactor.demos.in.action.webclient;

import java.util.List;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Simon Baslé
 */
@RestController
public class ClientExample {

	private WebClient swapi = WebClient.create("http://swapi.co/api/");

	private Flux<Map> findCharacters(String characterName) {
		//noinspection unchecked
		return swapi.get()
		         .uri("/people/?search={charName}", characterName)
		         .retrieve()
		         .bodyToMono(Map.class)
		            .flatMapMany(results -> Flux.fromIterable(
				            (List<Map>) results.get("results")));
	}

	private Mono<Map> getCharacter(int characterId) {
		return swapi.get()
		            .uri("/people/{id}", characterId)
		            .retrieve()
		            .bodyToMono(Map.class);
	}

	private Flux<String> fetchMovieTitles(Map characterJson) {
		if (characterJson.containsKey("films")) {
			@SuppressWarnings("unchecked")
			Iterable<String> movieUris = (Iterable<String>) characterJson.get("films");
			return Flux.fromIterable(movieUris)
			           .concatMap(url -> swapi.get().uri(url)
			                                .retrieve()
			                                .bodyToMono(Map.class)
			                                .map(map -> String.valueOf(map.get("title")))
			           );
		}
		else {
			return Flux.error(new IllegalArgumentException("passed Map does not represent a character"));
		}
	}



	@GetMapping(value = "/api/starwars/person/{charId}"
			, produces = MediaType.TEXT_EVENT_STREAM_VALUE
	)
	public Flux<String> charInMoviesById(@PathVariable int charId) {
		return getCharacter(charId)
				.flatMapMany(json -> fetchMovieTitles(json)
						.map(title -> title + "\n")
						.startWith("Character " + charId + " is " + json.get("name") +
								", appears in movies:\n")
						.concatWith(Mono.just("and that's it!\n\n"))
				);
	}

	@GetMapping(value = "/api/starwars/person")
	public Flux<String> charInMoviesByName(@RequestParam String name) {
		return findCharacters(name)
				.flatMapSequential(json -> fetchMovieTitles(json)
						.map(title -> title + "\n")
						.startWith("Character " + json.get("name") +
								" appears in movies:\n")
						.concatWith(Mono.just("and that's it!\n\n"))
				);
	}

}
