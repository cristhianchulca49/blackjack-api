package com.blackjack.player.domain.port.in;

import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import reactor.core.publisher.Mono;

public interface GetPlayerUseCase {
    Mono<Player> execute(PlayerId playerId);
}
