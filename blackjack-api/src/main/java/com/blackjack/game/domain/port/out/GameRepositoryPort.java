package com.blackjack.game.domain.port.out;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.player.domain.model.PlayerId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameRepositoryPort {
    Mono<Game> save(Game game);

    Mono<Game> findById(GameId gameId);

    Flux<Game> findByPlayerId(PlayerId playerId);

    Mono<Void> deleteById(GameId gameId);

    Mono<Boolean> existsById(GameId gameId);
}