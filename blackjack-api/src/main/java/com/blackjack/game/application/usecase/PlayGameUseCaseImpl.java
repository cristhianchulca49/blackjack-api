package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.exception.DomainException;
import com.blackjack.game.domain.model.exception.InvalidMoveException;
import com.blackjack.game.domain.port.in.PlayGameUseCase;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.game.domain.service.BlackjackDomainService;
import com.blackjack.shared.application.exception.GameNotFoundException;
import reactor.core.publisher.Mono;

public class PlayGameUseCaseImpl implements PlayGameUseCase {
    private final GameRepositoryPort gameRepositoryPort;
    private final BlackjackDomainService blackjackDomainService;

    public PlayGameUseCaseImpl(GameRepositoryPort gameRepositoryPort, BlackjackDomainService blackjackDomainService) {
        this.gameRepositoryPort = gameRepositoryPort;
        this.blackjackDomainService = blackjackDomainService;
    }

    @Override
    public Mono<Game> execute(String gameId, String action) {
        return gameRepositoryPort.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(game -> applyAction(action, game))
                .flatMap(gameRepositoryPort::save);

    }

    private Mono<Game> applyAction(String action, Game game) {
        return Mono.fromCallable(
                () -> switch (action.toUpperCase()) {
                    case "HIT" -> blackjackDomainService.hit(game);
                    case "STAND" -> blackjackDomainService.stand(game);
                    default -> throw new InvalidMoveException(action);
                });
    }
}
