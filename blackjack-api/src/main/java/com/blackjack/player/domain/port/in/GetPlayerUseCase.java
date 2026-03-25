package com.blackjack.player.domain.port.in;

import com.blackjack.player.domain.model.Player;
import reactor.core.publisher.Mono;

public interface GetPlayerUseCase {
    Mono<Player> execute(String playerId);
}
