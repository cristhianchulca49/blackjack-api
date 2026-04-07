package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.event.GameFinishedEvent;
import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.valueObject.Card;
import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.game.domain.service.BlackjackDomainService;
import com.blackjack.player.domain.model.PlayerId;
import com.blackjack.shared.application.exception.ResourceNotFoundException;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class PlayGameUseCaseTest {

    @Mock
    private GameRepositoryPort gameRepositoryPort;

    @Mock
    private BlackjackDomainService blackjackDomainService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PlayGameUseCaseImpl playGameUseCase;

    @Test
    @DisplayName("Should play action and save game")
    void playActionTest() {
        GameId gameId = GameId.generate();
        PlayerId playerId = PlayerId.generate();
        String action = "HIT";
        
        Hand dealerHand = Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TWO), new Card(Card.Suit.DIAMONDS, Card.Rank.THREE));
        Hand playerHand = Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.FOUR), new Card(Card.Suit.SPADES, Card.Rank.FIVE));
        Game existingGame = Game.create(gameId, playerId, dealerHand, playerHand, Deck.reconstitute(List.of()), GameStatus.IN_PROGRESS);
        Game updatedGame = Game.create(gameId, playerId, dealerHand, playerHand, Deck.reconstitute(List.of()), GameStatus.PLAYER_WINS);

        Mockito.when(gameRepositoryPort.findById(eq(gameId))).thenReturn(Mono.just(existingGame));
        Mockito.when(blackjackDomainService.hit(any(Game.class))).thenReturn(updatedGame);
        Mockito.when(gameRepositoryPort.save(any(Game.class))).thenReturn(Mono.just(updatedGame));

        StepVerifier.create(playGameUseCase.execute(gameId, action))
                .expectNext(updatedGame)
                .verifyComplete();

        Mockito.verify(gameRepositoryPort).findById(gameId);
        Mockito.verify(blackjackDomainService).hit(existingGame);
        Mockito.verify(gameRepositoryPort).save(updatedGame);
        Mockito.verify(eventPublisher).publishEvent(any(GameFinishedEvent.class));
    }

    @Test
    @DisplayName("Should throw exception when game to play not found")
    void playGameNotFoundTest() {
        GameId gameId = GameId.generate();
        Mockito.when(gameRepositoryPort.findById(eq(gameId))).thenReturn(Mono.empty());

        StepVerifier.create(playGameUseCase.execute(gameId, "HIT"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
