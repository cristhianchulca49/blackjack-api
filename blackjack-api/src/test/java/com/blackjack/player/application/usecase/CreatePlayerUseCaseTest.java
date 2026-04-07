package com.blackjack.player.application.usecase;

import com.blackjack.player.domain.event.PlayerCreatedEvent;
import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.domain.port.out.PlayerRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CreatePlayerUseCaseTest {

    @Mock
    private PlayerRepositoryPort playerRepositoryPort;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CreatePlayerUseCaseImpl createPlayerUseCase;

    @Test
    @DisplayName("Should create a player and publish event")
    void createPlayerTest() {
        String name = "John Doe";
        PlayerId playerId = PlayerId.generate();
        Player player = Player.reconstitute(playerId, Name.of(name));

        Mockito.when(playerRepositoryPort.save(any(Player.class))).thenReturn(Mono.just(player));

        StepVerifier.create(createPlayerUseCase.execute(name))
                .expectNextMatches(savedPlayer -> {
                    return savedPlayer.getName().toString().equals(name) &&
                           savedPlayer.getPlayerId().equals(playerId);
                })
                .verifyComplete();

        Mockito.verify(playerRepositoryPort).save(any(Player.class));
        Mockito.verify(eventPublisher).publishEvent(any(PlayerCreatedEvent.class));
    }
}
