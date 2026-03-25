package com.blackjack.game.domain.port.in;

import com.blackjack.game.domain.model.Game;
import reactor.core.publisher.Mono;

/**
 * Inbound port: Use case to retrieve an existing game.
 * 
 * Responsibility: Fetch a game by ID from the repository.
 * 
 * Flow:
 * 1. Receive gameId
 * 2. Query repository
 * 3. Throw GameNotFoundException if not found
 * 4. Return Game
 */
public interface GetGameUseCase {
    
    /**
     * Retrieves a game by its ID.
     * 
     * @param gameId the ID of the game to retrieve
     * @return Mono<Game> the game
     * @throws com.blackjack.shared.domain.exception.GameNotFoundException if game not found
     */
    Mono<Game> execute(String gameId);
}

