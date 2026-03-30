package com.blackjack.game.domain.port.in;

import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.shared.application.exception.GameNotFoundException;
import reactor.core.publisher.Mono;

/**
 * Inbound port: Use case to delete a game.
 * 
 * Responsibility: Remove a game from the repository.
 * 
 * Flow:
 * 1. Receive gameId
 * 2. Delete from repository
 * 3. Return Mono<Void> (completion signal)
 */
public interface DeleteGameUseCase {
    
    /**
     * Deletes a game by its ID.
     * 
     * @param gameId the ID of the game to delete
     * @return Mono<Void> completion signal
     * @throws GameNotFoundException if game not found
     */
    Mono<Void> execute(GameId gameId);
}

