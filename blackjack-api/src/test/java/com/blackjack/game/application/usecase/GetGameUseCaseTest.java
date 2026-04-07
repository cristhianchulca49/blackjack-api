package com.blackjack.game.application.usecase;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.valueObject.Card;
import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameId;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import com.blackjack.game.domain.port.out.GameRepositoryPort;
import com.blackjack.player.domain.model.PlayerId;
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

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class GetGameUseCaseTest {

    @Mock
    private GameRepositoryPort gameRepositoryPort;

    @InjectMocks
    private GetGameUseCaseImpl getGameUseCase;

    @Test
    @DisplayName("Should get game by id when exists")
    void getGameTest() {
        GameId gameId = GameId.generate();
        PlayerId playerId = PlayerId.generate();
        Hand dealerHand = Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TWO), new Card(Card.Suit.DIAMONDS, Card.Rank.THREE));
        Hand playerHand = Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.FOUR), new Card(Card.Suit.SPADES, Card.Rank.FIVE));
        Game game = Game.create(gameId, playerId, dealerHand, playerHand, Deck.reconstitute(List.of()), GameStatus.IN_PROGRESS);

        Mockito.when(gameRepositoryPort.findById(eq(gameId))).thenReturn(Mono.just(game));

        StepVerifier.create(getGameUseCase.execute(gameId))
                .expectNext(game)
                .verifyComplete();

        Mockito.verify(gameRepositoryPort).findById(gameId);
    }

    @Test
    @DisplayName("Should throw exception when game not found")
    void getGameNotFoundTest() {
        GameId gameId = GameId.generate();
        Mockito.when(gameRepositoryPort.findById(eq(gameId))).thenReturn(Mono.empty());

        StepVerifier.create(getGameUseCase.execute(gameId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
