package com.blackjack.player.domain.port.in;

import com.blackjack.player.domain.model.PlayerId;
import reactor.core.publisher.Mono;

public interface UpdatePlayerNameUseCase {
    Mono<Void> execute(PlayerId playerId, String newName);
}
