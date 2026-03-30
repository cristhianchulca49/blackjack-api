package com.blackjack.player.domain.port.out;

import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepositoryPort {
    Mono<Player> save(Player player);

    Mono<Player> findById(PlayerId playerId);

    Mono<Void> deleteById(PlayerId playerId);
}