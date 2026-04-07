package com.blackjack.game.infrastructure.adapter.in.rest;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.valueObject.Card;
import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.game.domain.port.in.CreateGameUseCase;
import com.blackjack.game.domain.port.in.DeleteGameUseCase;
import com.blackjack.game.domain.port.in.GetGameUseCase;
import com.blackjack.game.domain.port.in.PlayGameUseCase;
import com.blackjack.game.infrastructure.adapter.in.rest.dto.request.PlayGameRequest;
import com.blackjack.player.domain.model.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(GameController.class)
class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateGameUseCase createGameUseCase;

    @MockitoBean
    private GetGameUseCase getGameUseCase;

    @MockitoBean
    private PlayGameUseCase playGameUseCase;

    @MockitoBean
    private DeleteGameUseCase deleteGameUseCase;

    private Game mockGame;
    private GameId gameId;
    private PlayerId playerId;

    @BeforeEach
    void setUp() {
        gameId = GameId.generate();
        playerId = PlayerId.generate();
        
        Card card1 = new Card(Card.Suit.CLUBS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN);
        Card card3 = new Card(Card.Suit.HEARTS, Card.Rank.TWO);
        Card card4 = new Card(Card.Suit.SPADES, Card.Rank.FIVE);
        
        Hand playerHand = Hand.of(card1, card2);
        Hand dealerHand = Hand.of(card3, card4);
        Deck deck = Deck.reconstitute(List.of());
        
        mockGame = Game.create(gameId, playerId, dealerHand, playerHand, deck, GameStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Should create a game for a player")
    void createGameTest() {
        Mockito.when(createGameUseCase.execute(eq(playerId))).thenReturn(Mono.just(mockGame));

        webTestClient.post()
                .uri("/api/games/player/{playerId}", playerId.getValue())
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/api/games/" + gameId.getValue())
                .expectBody()
                .jsonPath("$.id").isEqualTo(gameId.getValue())
                .jsonPath("$.status").isEqualTo("IN_PROGRESS");
    }

    @Test
    @DisplayName("Should get a game by id")
    void getGameTest() {
        Mockito.when(getGameUseCase.execute(eq(gameId))).thenReturn(Mono.just(mockGame));

        webTestClient.get()
                .uri("/api/games/{id}", gameId.getValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(gameId.getValue());
    }

    @Test
    @DisplayName("Should play an action in a game")
    void playActionTest() {
        String action = "HIT";
        Mockito.when(playGameUseCase.execute(eq(gameId), eq(action))).thenReturn(Mono.just(mockGame));

        webTestClient.post()
                .uri("/api/games/{id}/play", gameId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayGameRequest(action))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(gameId.getValue());
    }

    @Test
    @DisplayName("Should delete a game")
    void deleteGameTest() {
        Mockito.when(deleteGameUseCase.execute(eq(gameId))).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/games/{id}", gameId.getValue())
                .exchange()
                .expectStatus().isNoContent();
    }
}
