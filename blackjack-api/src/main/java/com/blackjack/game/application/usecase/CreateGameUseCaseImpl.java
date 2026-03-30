package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.exception.DomainException;
import com.blackjack.game.domain.port.in.CreateGameUseCase;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.game.domain.service.BlackjackDomainService;
import com.blackjack.player.domain.port.out.PlayerRepositoryPort;
import com.blackjack.shared.application.exception.PlayerNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateGameUseCaseImpl implements CreateGameUseCase {

    private final GameRepositoryPort gameRepositoryPort;
    private final PlayerRepositoryPort playerRepositoryPort;
    private final BlackjackDomainService blackjackDomainService;

    public CreateGameUseCaseImpl(GameRepositoryPort gameRepositoryPort, PlayerRepositoryPort playerRepositoryPort,
                                 BlackjackDomainService blackjackDomainService) {
        this.gameRepositoryPort = gameRepositoryPort;
        this.playerRepositoryPort = playerRepositoryPort;
        this.blackjackDomainService = blackjackDomainService;
    }

    @Override
    public Mono<Game> execute(String playerId) {
        return playerRepositoryPort.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException(playerId)))
                .flatMap(player ->
                        Mono.fromCallable(() -> blackjackDomainService.dealInitialCards(playerId))
                                .onErrorMap(
                                        ex -> !(ex instanceof PlayerNotFoundException),
                                        ex -> new DomainException(ex.getMessage())
                                )
                                .flatMap(gameRepositoryPort::save)
                );
    }
}
