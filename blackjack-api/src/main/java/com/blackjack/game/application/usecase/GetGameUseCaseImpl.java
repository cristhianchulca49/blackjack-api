package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.port.in.GetGameUseCase;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetGameUseCaseImpl implements GetGameUseCase {
    private final GameRepositoryPort gameRepositoryPort;

    public GetGameUseCaseImpl(GameRepositoryPort gameRepositoryPort) {
        this.gameRepositoryPort = gameRepositoryPort;
    }

    @Override
    public Mono<Game> execute(GameId gameId) {
        return gameRepositoryPort.findById(gameId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException("Game", gameId.getValue())));
    }
}
