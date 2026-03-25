package com.blackjack.game.domain.port.in;

import com.blackjack.game.domain.model.Game;
import reactor.core.publisher.Mono;

/**
 * Inbound port: Use case to create a new game.
 * 
 * Responsibility: Create a new game for a player with initial cards dealt.
 * 
 * Flow:
 * 1. Receive playerId
 * 2. Create Game aggregate with initial hands (2 cards each)
 * 3. Persist game to repository
 * 4. Return created Game
 */
public interface CreateGameUseCase {
    
    /**
     * Creates a new game for the given player.
     * 
     * @param playerId the ID of the player starting the game
     * @return Mono<Game> the created game with initial cards dealt
     */
    Mono<Game> execute(String playerId);
}

