package com.blackjack.game.domain.service;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.valueObject.*;
import com.blackjack.player.domain.model.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BlackjackDomainService Tests")
class BlackjackDomainServiceTest {

    private BlackjackDomainService domainService;
    private final PlayerId playerId = PlayerId.generate();

    @BeforeEach
    void setUp() {
        domainService = new BlackjackDomainService();
    }

    // ========== dealInitialCards() ==========

    @Test
    @DisplayName("should deal 2 cards to player and 2 to dealer")
    void shouldDealInitialCards() {
        Game dealt = domainService.dealInitialCards(playerId);

        assertThat(dealt.getPlayerHand().getCards()).hasSize(2);
        assertThat(dealt.getDealerHand().getCards()).hasSize(2);
        assertThat(dealt.getPlayerId()).isEqualTo(playerId);
        assertThat(dealt.getStatus()).isNotNull();
    }

    // ========== hit() ==========

    @Test
    @DisplayName("should add one card to player hand on HIT")
    void shouldAddOneCardToPlayerOnHit() {
        Game game = Game.create(
                GameId.generate(),
                playerId,
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.KING), new Card(Card.Suit.SPADES, Card.Rank.TWO)),
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.FIVE), new Card(Card.Suit.DIAMONDS, Card.Rank.SIX)),
                Deck.fullShuffled(),
                GameStatus.IN_PROGRESS
        );
        int initialCardsCount = game.getPlayerHand().getCards().size();

        Game afterHit = domainService.hit(game);

        assertThat(afterHit.getPlayerHand().getCards()).hasSize(initialCardsCount + 1);
    }

    @Test
    @DisplayName("should set status to DEALER_WINS if player busts on HIT")
    void shouldSetDealerWinsWhenPlayerBusts() {
        Hand playerHand = Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN));
        Deck riggedDeck = Deck.reconstitute(java.util.List.of(new Card(Card.Suit.SPADES, Card.Rank.TEN)));

        Game game = Game.create(
                GameId.generate(),
                playerId,
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.FIVE), new Card(Card.Suit.SPADES, Card.Rank.SIX)),
                playerHand,
                riggedDeck,
                GameStatus.IN_PROGRESS
        );

        Game afterHit = domainService.hit(game);

        assertThat(afterHit.getPlayerHand().isBust()).isTrue();
        assertThat(afterHit.getStatus()).isEqualTo(GameStatus.DEALER_WINS);
    }

    // ========== stand() and resolveDealerTurn() ==========

    @Test
    @DisplayName("should dealer draw until score >= 17")
    void shouldDealerDrawUntilScoreGreaterOrEqualTo17() {
        Hand dealerHand = Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TWO), new Card(Card.Suit.SPADES, Card.Rank.THREE));
        Deck riggedDeck = Deck.reconstitute(java.util.List.of(
                new Card(Card.Suit.HEARTS, Card.Rank.TEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.TEN)
        ));

        Game game = Game.create(
                GameId.generate(),
                playerId,
                dealerHand,
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.KING)),
                riggedDeck,
                GameStatus.IN_PROGRESS
        );

        Game afterStand = domainService.stand(game);

        assertThat(afterStand.getDealerHand().score()).isGreaterThanOrEqualTo(17);
        assertThat(afterStand.getStatus()).isNotEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("should set PLAYER_WINS if dealer busts")
    void shouldSetPlayerWinsWhenDealerBusts() {
        Hand dealerHand = Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TEN), new Card(Card.Suit.SPADES, Card.Rank.JACK));
        Deck riggedDeck = Deck.reconstitute(java.util.List.of(new Card(Card.Suit.HEARTS, Card.Rank.KING)));

        Game game = Game.create(
                GameId.generate(),
                playerId,
                dealerHand,
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.KING)),
                riggedDeck,
                GameStatus.IN_PROGRESS
        );

        Game gameWithDealer15 = Game.create(
                game.getId(), playerId,
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.SEVEN), new Card(Card.Suit.SPADES, Card.Rank.EIGHT)),
                game.getPlayerHand(), riggedDeck, GameStatus.IN_PROGRESS
        );

        Game afterStand = domainService.stand(gameWithDealer15);

        assertThat(afterStand.getDealerHand().isBust()).isTrue();
        assertThat(afterStand.getStatus()).isEqualTo(GameStatus.PLAYER_WINS);
    }

    @Test
    @DisplayName("should set DRAW if both have same score")
    void shouldSetDrawIfSameScore() {
        Game game = Game.create(
                GameId.generate(),
                playerId,
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.KING), new Card(Card.Suit.SPADES, Card.Rank.QUEEN)), // 20
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.TEN), new Card(Card.Suit.DIAMONDS, Card.Rank.JACK)), // 20
                Deck.fullShuffled(),
                GameStatus.IN_PROGRESS
        );

        Game afterStand = domainService.stand(game);

        if (!afterStand.getDealerHand().isBust() && afterStand.getDealerHand().score() == 20) {
            assertThat(afterStand.getStatus()).isEqualTo(GameStatus.DRAW);
        }
    }

    @Test
    @DisplayName("should not modify original game (immutable pattern)")
    void shouldNotModifyOriginalGame() {
        Game game1 = domainService.dealInitialCards(playerId);
        GameStatus originalStatus = game1.getStatus();

        if (originalStatus == GameStatus.IN_PROGRESS) {
            domainService.stand(game1);
            assertThat(game1.getStatus()).isEqualTo(originalStatus);
        }
    }
}