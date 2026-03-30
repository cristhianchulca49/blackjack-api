package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.port.in.GetGameByPlayerIdUseCase;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.domain.port.out.PlayerRepositoryPort;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetGameByPlayerIdUseCaseImpl implements GetGameByPlayerIdUseCase {
    private final GameRepositoryPort gameRepositoryPort;
    private final PlayerRepositoryPort playerRepositoryPort;

    public GetGameByPlayerIdUseCaseImpl(GameRepositoryPort gameRepositoryPort, PlayerRepositoryPort playerRepositoryPort) {
        this.gameRepositoryPort = gameRepositoryPort;
        this.playerRepositoryPort = playerRepositoryPort;
    }

    @Override
    public Flux<Game> execute(PlayerId playerId) {
        return playerRepositoryPort.findById(playerId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException("Player", playerId.getValue())))
                .flatMapMany(player -> gameRepositoryPort.findByPlayerId(playerId));
    }
}
