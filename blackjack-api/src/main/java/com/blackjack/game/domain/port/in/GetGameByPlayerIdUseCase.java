package com.blackjack.game.domain.port.in;

import com.blackjack.game.domain.model.Game;
import com.blackjack.player.domain.model.PlayerId;
import reactor.core.publisher.Flux;

/**
 * Inbound port: Use case to retrieve all games for a player.
 * 
 * Responsibility: Fetch all games by player ID from the repository.
 * 
 * Flow:
 * 1. Receive playerId
 * 2. Query repository
 * 3. Return all games for that player (may be empty)
 */
public interface GetGameByPlayerIdUseCase {
    
    /**
     * Retrieves all games for a player.
     * 
     * @param playerId the ID of the player
     * @return Flux<Game> all games for that player (empty if none found)
     */
    Flux<Game> execute(PlayerId playerId);
}

