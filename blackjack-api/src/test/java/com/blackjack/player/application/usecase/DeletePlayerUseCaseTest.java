package com.blackjack.player.application.usecase;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.domain.port.out.PlayerRepositoryPort;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class DeletePlayerUseCaseTest {

    @Mock
    private PlayerRepositoryPort playerRepositoryPort;

    @InjectMocks
    private DeletePlayerUseCaseImpl deletePlayerUseCase;

    @Test
    @DisplayName("Should delete player when player exists")
    void deletePlayerTest() {
        PlayerId playerId = PlayerId.generate();
        Player player = Player.reconstitute(playerId, Name.of("To Delete"));

        Mockito.when(playerRepositoryPort.findById(eq(playerId))).thenReturn(Mono.just(player));
        Mockito.when(playerRepositoryPort.deleteById(eq(playerId))).thenReturn(Mono.empty());

        StepVerifier.create(deletePlayerUseCase.execute(playerId))
                .verifyComplete();

        Mockito.verify(playerRepositoryPort).findById(playerId);
        Mockito.verify(playerRepositoryPort).deleteById(playerId);
    }

    @Test
    @DisplayName("Should throw exception when player to delete not found")
    void deletePlayerNotFoundTest() {
        PlayerId playerId = PlayerId.generate();
        Mockito.when(playerRepositoryPort.findById(eq(playerId))).thenReturn(Mono.empty());

        StepVerifier.create(deletePlayerUseCase.execute(playerId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
