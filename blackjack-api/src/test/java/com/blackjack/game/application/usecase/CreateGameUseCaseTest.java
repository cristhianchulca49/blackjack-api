package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.valueObject.Card;
import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.game.domain.service.BlackjackDomainService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class CreateGameUseCaseTest {

    @Mock
    private GameRepositoryPort gameRepositoryPort;

    @Mock
    private PlayerRepositoryPort playerRepositoryPort;

    @Mock
    private BlackjackDomainService blackjackDomainService;

    @InjectMocks
    private CreateGameUseCaseImpl createGameUseCase;

    @Test
    @DisplayName("Should create a game when player exists")
    void createGameTest() {
        PlayerId playerId = PlayerId.generate();
        Player player = Player.reconstitute(playerId, Name.of("John Doe"));
        
        GameId gameId = GameId.generate();
        Hand dealerHand = Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TWO), new Card(Card.Suit.DIAMONDS, Card.Rank.THREE));
        Hand playerHand = Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.FOUR), new Card(Card.Suit.SPADES, Card.Rank.FIVE));
        Game mockGame = Game.create(gameId, playerId, dealerHand, playerHand, Deck.reconstitute(List.of()), GameStatus.IN_PROGRESS);

        Mockito.when(playerRepositoryPort.findById(eq(playerId))).thenReturn(Mono.just(player));
        Mockito.when(blackjackDomainService.dealInitialCards(eq(playerId))).thenReturn(mockGame);
        Mockito.when(gameRepositoryPort.save(any(Game.class))).thenReturn(Mono.just(mockGame));

        StepVerifier.create(createGameUseCase.execute(playerId))
                .expectNext(mockGame)
                .verifyComplete();

        Mockito.verify(playerRepositoryPort).findById(playerId);
        Mockito.verify(blackjackDomainService).dealInitialCards(playerId);
        Mockito.verify(gameRepositoryPort).save(mockGame);
    }

    @Test
    @DisplayName("Should throw exception when player not found")
    void createGamePlayerNotFoundTest() {
        PlayerId playerId = PlayerId.generate();
        Mockito.when(playerRepositoryPort.findById(eq(playerId))).thenReturn(Mono.empty());

        StepVerifier.create(createGameUseCase.execute(playerId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
