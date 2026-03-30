package com.blackjack.player.application.usecase;

import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.domain.port.in.DeletePlayerUseCase;
import com.blackjack.player.domain.port.out.PlayerRepositoryPort;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeletePlayerUseCaseImpl implements DeletePlayerUseCase {

    private final PlayerRepositoryPort playerRepositoryPort;

    public DeletePlayerUseCaseImpl(PlayerRepositoryPort playerRepositoryPort) {
        this.playerRepositoryPort = playerRepositoryPort;
    }

    @Override
    public Mono<Void> execute(PlayerId playerId) {
        return playerRepositoryPort.findById(playerId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException("Player", playerId.getValue())))
                .flatMap(player -> playerRepositoryPort.deleteById(playerId));
    }
}

