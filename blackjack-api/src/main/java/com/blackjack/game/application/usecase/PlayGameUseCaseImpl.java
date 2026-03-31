package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.exception.InvalidMoveException;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.game.domain.port.in.PlayGameUseCase;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.game.domain.service.BlackjackDomainService;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import com.blackjack.game.domain.event.GameFinishedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PlayGameUseCaseImpl implements PlayGameUseCase {
    private final GameRepositoryPort gameRepositoryPort;
    private final BlackjackDomainService blackjackDomainService;
    private final ApplicationEventPublisher eventPublisher;

    public PlayGameUseCaseImpl(GameRepositoryPort gameRepositoryPort, BlackjackDomainService blackjackDomainService, ApplicationEventPublisher eventPublisher) {
        this.gameRepositoryPort = gameRepositoryPort;
        this.blackjackDomainService = blackjackDomainService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<Game> execute(GameId gameId, String action) {
        return gameRepositoryPort.findById(gameId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException("Game", gameId.getValue())))
                .flatMap(game -> applyAction(action, game))
                .flatMap(gameRepositoryPort::save)
                .doOnNext(game -> {
                    if (game.getStatus() != null && game.getStatus() != GameStatus.IN_PROGRESS && game.getStatus() != GameStatus.DEALER_WINS) {
                        eventPublisher.publishEvent(new GameFinishedEvent(game.getPlayerId(), game.getStatus()));
                    }
                });

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
