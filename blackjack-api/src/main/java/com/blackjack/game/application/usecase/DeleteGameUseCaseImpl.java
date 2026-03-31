package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.port.in.DeleteGameUseCase;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class DeleteGameUseCaseImpl implements DeleteGameUseCase {
    private final GameRepositoryPort gameRepositoryPort;

    public DeleteGameUseCaseImpl(GameRepositoryPort gameRepositoryPort) {
        this.gameRepositoryPort = gameRepositoryPort;
    }

    @Override
    public Mono<Void> execute(GameId gameId) {
        return gameRepositoryPort.findById(gameId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException("Game", gameId.getValue())))
                .flatMap(game -> gameRepositoryPort.deleteById(gameId));
    }
}
