package com.blackjack.player.domain.port.in;

import com.blackjack.player.domain.model.PlayerId;
import reactor.core.publisher.Mono;

public interface DeletePlayerUseCase {
    Mono<Void> execute(PlayerId playerId);
}