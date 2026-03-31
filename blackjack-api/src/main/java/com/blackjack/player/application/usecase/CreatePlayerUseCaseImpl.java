package com.blackjack.player.application.usecase;

import com.blackjack.player.domain.event.PlayerCreatedEvent;
import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.port.in.CreatePlayerUseCase;
import com.blackjack.player.domain.port.out.PlayerRepositoryPort;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreatePlayerUseCaseImpl implements CreatePlayerUseCase {

    private final PlayerRepositoryPort playerRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;

    public CreatePlayerUseCaseImpl(PlayerRepositoryPort playerRepositoryPort, ApplicationEventPublisher eventPublisher) {
        this.playerRepositoryPort = playerRepositoryPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<Player> execute(String name) {
        return Mono.fromCallable(() -> Player.create(name))
                .flatMap(playerRepositoryPort::save)
                .doOnNext(player -> {
                    eventPublisher.publishEvent(new PlayerCreatedEvent(player.getPlayerId(), player.getName()));
                });
    }
}

