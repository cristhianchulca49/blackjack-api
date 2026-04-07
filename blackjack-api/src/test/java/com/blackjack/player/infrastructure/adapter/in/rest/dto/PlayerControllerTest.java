package com.blackjack.player.infrastructure.adapter.in.rest.dto;

import com.blackjack.player.domain.model.Name;
import com.blackjack.player.domain.model.Player;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.player.domain.port.in.CreatePlayerUseCase;
import com.blackjack.player.domain.port.in.DeletePlayerUseCase;
import com.blackjack.player.domain.port.in.UpdatePlayerNameUseCase;
import com.blackjack.player.infrastructure.adapter.in.rest.dto.request.PlayerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreatePlayerUseCase createPlayerUseCase;

    @MockitoBean
    private UpdatePlayerNameUseCase updatePlayerNameUseCase;

    @MockitoBean
    private DeletePlayerUseCase deletePlayerUseCase;

    @Test
    @DisplayName("Should create a player")
    void createPlayerTest() {
        String name = "John Doe";
        PlayerId playerId = PlayerId.generate();
        Player player = Player.reconstitute(playerId, Name.of(name));

        Mockito.when(createPlayerUseCase.execute(name)).thenReturn(Mono.just(player));

        webTestClient.post()
                .uri("/api/player")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerRequest(name))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/api/player/" + playerId.getValue())
                .expectBody()
                .jsonPath("$.id").isEqualTo(playerId.getValue())
                .jsonPath("$.name").isEqualTo(name);
    }

    @Test
    @DisplayName("Should update a player name")
    void updatePlayerNameTest() {
        String playerIdStr = UUID.randomUUID().toString();
        PlayerId playerId = PlayerId.reconstitute(playerIdStr);
        String newName = "Jane Doe";
        Player updatedPlayer = Player.reconstitute(playerId, Name.of(newName));

        Mockito.when(updatePlayerNameUseCase.execute(eq(playerId), eq(newName)))
                .thenReturn(Mono.just(updatedPlayer));

        webTestClient.put()
                .uri("/api/player/{playerId}", playerIdStr)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerRequest(newName))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(playerIdStr)
                .jsonPath("$.name").isEqualTo(newName);
    }

    @Test
    @DisplayName("Should delete a player")
    void deletePlayerTest() {
        String playerIdStr = UUID.randomUUID().toString();
        PlayerId playerId = PlayerId.reconstitute(playerIdStr);

        Mockito.when(deletePlayerUseCase.execute(eq(playerId))).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/player/{playerId}", playerIdStr)
                .exchange()
                .expectStatus().isNoContent();
    }
}
