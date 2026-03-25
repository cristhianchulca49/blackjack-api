package com.blackjack.game.domain.port.in;

import com.blackjack.game.domain.model.Game;
import reactor.core.publisher.Mono;

/**
 * Inbound port: Use case to play an action in an ongoing game.
 * 
 * Responsibility: Execute a game action (HIT or STAND) and update game state.
 * 
 * Flow:
 * 1. Receive gameId and action (HIT|STAND)
 * 2. Retrieve game from repository
 * 3. Validate game is not finished
 * 4. Execute action via BlackjackDomainService
 * 5. Persist updated game
 * 6. Return updated Game
 */
public interface PlayGameUseCase {
    
    /**
     * Plays an action in the game (HIT or STAND).
     * 
     * @param gameId the ID of the game
     * @param action the action to play: "HIT" or "STAND"
     * @return Mono<Game> the updated game after action
     * @throws com.blackjack.shared.domain.exception.GameNotFoundException if game not found
     * @throws com.blackjack.shared.domain.exception.GameAlreadyFinishedException if game is finished
     * @throws com.blackjack.shared.domain.exception.InvalidMoveException if action is invalid
     */
    Mono<Game> execute(String gameId, String action);
}

