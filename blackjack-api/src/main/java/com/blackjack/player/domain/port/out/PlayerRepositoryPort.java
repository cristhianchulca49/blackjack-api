package com.blackjack.player.domain.port.out;

import com.blackjack.player.domain.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepositoryPort {
    Mono<Player> save(Player player);

    Mono<Player> findById(String playerId);

    Flux<Player> findAll();

    Mono<Void> deleteById(String playerId);
}