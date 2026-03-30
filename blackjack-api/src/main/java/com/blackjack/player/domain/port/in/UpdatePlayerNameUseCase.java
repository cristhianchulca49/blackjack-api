package com.blackjack.player.domain.port.in;

import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import reactor.core.publisher.Mono;

public interface UpdatePlayerNameUseCase {
    Mono<Player> execute(PlayerId playerId, String newName);
}
