package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.port.in.CreateGameUseCase;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.game.domain.service.BlackjackDomainService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateGameUseCaseImpl implements CreateGameUseCase {

    private final GameRepositoryPort gameRepositoryPort;
    private final BlackjackDomainService blackjackDomainService;

    public CreateGameUseCaseImpl(GameRepositoryPort gameRepositoryPort,
                                 BlackjackDomainService blackjackDomainService) {
        this.gameRepositoryPort = gameRepositoryPort;
        this.blackjackDomainService = blackjackDomainService;
    }

    @Override
    public Mono<Game> execute(String playerId) {
        return Mono.fromCallable(() -> blackjackDomainService.dealInitialCards(playerId))
                .flatMap(gameRepositoryPort::save);
    }
}
