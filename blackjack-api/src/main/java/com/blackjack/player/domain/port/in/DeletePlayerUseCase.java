package com.blackjack.player.domain.port.in;

import reactor.core.publisher.Mono;

public interface DeletePlayerUseCase {
    Mono<Void> execute(String playerId);
}