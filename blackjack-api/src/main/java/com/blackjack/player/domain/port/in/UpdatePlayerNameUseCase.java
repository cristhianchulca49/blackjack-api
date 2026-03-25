package com.blackjack.player.domain.port.in;

import reactor.core.publisher.Mono;

public interface UpdatePlayerNameUseCase {
    Mono<Void> execute(String playerId, String newName);
}
