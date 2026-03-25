package com.blackjack.game.domain.port.out;

import com.blackjack.game.domain.model.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameRepositoryPort {
    Mono<Game> save(Game game);                          // Insert/Update
    Mono<Game> findById(String gameId);                  // Recuperar por ID
    Flux<Game> findByPlayerId(String playerId);          // Todos los juegos de un jugador
    Mono<Void> deleteById(String gameId);                // Eliminar
    Mono<Boolean> existsById(String gameId);             // Verificar existencia
}