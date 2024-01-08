package com.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class UserController {

    private Map<String, List<String>> movies = Map.of(
            "free-tier", List.of("Shawshank Redemption", "Titanic", "Avatar", "Inception"),
            "premium", List.of("The Dark Knight", "Pulp Fiction", "Forrest Gump", "Fight Club"),
            "ultimate", List.of("The Matrix", "Interstellar", "Se7en", "The Lion King", "The Departed", "The Prestige", "The Usual Suspects")
    );

    @GetMapping("/free-tier")
    @PreAuthorize("hasAnyRole('ROLE_free-tier', 'ROLE_premium', 'ROLE_ultimate')")
    public Mono<List<String>> freeTierUser() {
        return Mono.just(movies.get("free-tier"));
    }

    @GetMapping("/premium")
    @PreAuthorize("hasAnyRole('ROLE_premium', 'ROLE_ultimate')")
    public Mono<List<String>> premium(Authentication authentication) {
        return Mono.just(Stream.concat(movies.get("free-tier").stream(), movies.get("premium").stream()).toList());
    }

    @GetMapping("/ultimate")
    @PreAuthorize("hasRole('ROLE_ultimate')")
    public Mono<List<String>> ultimate() {
        return Mono.just(
                Stream.concat(
                        Stream.concat(
                                movies.get("free-tier").stream(),
                                movies.get("premium").stream()
                        ).toList().stream(), movies.get("ultimate").stream()).toList());
    }

}
