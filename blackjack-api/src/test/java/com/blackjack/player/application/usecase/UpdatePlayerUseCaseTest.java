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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UpdatePlayerUseCaseTest {

    @Mock
    private PlayerRepositoryPort playerRepositoryPort;

    @InjectMocks
    private UpdatePlayerUseCaseImpl updatePlayerUseCase;

    @Test
    @DisplayName("Should update player name when player exists")
    void updatePlayerNameTest() {
        PlayerId playerId = PlayerId.generate();
        String oldName = "Old Name";
        String newName = "New Name";
        Player existingPlayer = Player.reconstitute(playerId, Name.of(oldName));
        Player updatedPlayer = Player.reconstitute(playerId, Name.of(newName));

        Mockito.when(playerRepositoryPort.findById(eq(playerId))).thenReturn(Mono.just(existingPlayer));
        Mockito.when(playerRepositoryPort.save(any(Player.class))).thenReturn(Mono.just(updatedPlayer));

        StepVerifier.create(updatePlayerUseCase.execute(playerId, newName))
                .expectNextMatches(player -> player.getName().toString().equals(newName))
                .verifyComplete();

        Mockito.verify(playerRepositoryPort).findById(playerId);
        Mockito.verify(playerRepositoryPort).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception when player not found")
    void updatePlayerNotFoundTest() {
        PlayerId playerId = PlayerId.generate();
        Mockito.when(playerRepositoryPort.findById(eq(playerId))).thenReturn(Mono.empty());

        StepVerifier.create(updatePlayerUseCase.execute(playerId, "New Name"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
