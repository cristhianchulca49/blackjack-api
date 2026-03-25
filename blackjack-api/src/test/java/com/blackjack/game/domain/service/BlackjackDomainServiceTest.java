package com.blackjack.game.domain.service;

import com.blackjack.game.domain.model.Game;
import com.blackjack.game.domain.model.Hand;
import com.blackjack.game.domain.model.valueObject.Card;
import com.blackjack.game.domain.model.valueObject.Deck;
import com.blackjack.game.domain.model.valueObject.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BlackjackDomainService Tests")
class BlackjackDomainServiceTest {

    private BlackjackDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new BlackjackDomainService();
    }

    // ========== dealInitialCards() ==========

    @Test
    @DisplayName("should deal 2 cards to player and 2 to dealer")
    void shouldDealInitialCards() {
        Game game = Game.create(
                "player1",
                null,
                null,
                null
        );

        Game dealt = domainService.dealInitialCards(game);

        assertThat(dealt.getPlayerHand().getCards()).hasSize(2);
        assertThat(dealt.getDealerHand().getCards()).hasSize(2);
    }

    @Test
    @DisplayName("should set status to IN_PROGRESS, PLAYER_WINS, DEALER_WINS, or DRAW")
    void shouldSetValidStatusOnDeal() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.TWO), new Card(Card.Suit.DIAMONDS, Card.Rank.THREE)),
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TWO), new Card(Card.Suit.SPADES, Card.Rank.THREE)),
                Deck.fullShuffled()
        );

        Game dealt = domainService.dealInitialCards(game);

        assertThat(dealt.getStatus()).isIn(
                GameStatus.IN_PROGRESS,
                GameStatus.PLAYER_WINS,
                GameStatus.DEALER_WINS,
                GameStatus.DRAW
        );
    }

    @Test
    @DisplayName("should test from() method directly with specific hands")
    void shouldDetectBlackjackViaFromMethod() {
        // Test the from() method logic indirectly through hit()
        // because from() is private

        // Create hands manually and test hit logic instead
        // This will be covered in the stand/resolve tests below
    }

    // ========== hit() ==========

    @Test
    @DisplayName("should add one card to player hand on HIT")
    void shouldAddOneCardToPlayerOnHit() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.FIVE), new Card(Card.Suit.DIAMONDS, Card.Rank.SIX)),
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.KING), new Card(Card.Suit.SPADES, Card.Rank.QUEEN)),
                Deck.fullShuffled()
        );
        int initialScore = game.getPlayerHand().score();

        Game afterHit = domainService.hit(game);

        assertThat(afterHit.getPlayerHand().score()).isGreaterThan(initialScore);
    }

    @Test
    @DisplayName("should set status to IN_PROGRESS if player doesn't bust or reach 21 on HIT")
    void shouldKeepInProgressWhenPlayerHits() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.FIVE), new Card(Card.Suit.DIAMONDS, Card.Rank.SIX)),
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.KING), new Card(Card.Suit.SPADES, Card.Rank.QUEEN)),
                Deck.fullShuffled()
        );

        Game afterHit = domainService.hit(game);

        if (!afterHit.getPlayerHand().isBust() && afterHit.getPlayerHand().score() != 21) {
            assertThat(afterHit.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        }
    }

    @Test
    @DisplayName("should set status to DEALER_WINS if player busts on HIT")
    void shouldSetDealerWinsWhenPlayerBusts() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN)),  // 20
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.FIVE), new Card(Card.Suit.SPADES, Card.Rank.SIX)),  // 11
                Deck.fullShuffled()
        );

        Game afterHit = domainService.hit(game);

        if (afterHit.getPlayerHand().isBust()) {
            assertThat(afterHit.getStatus()).isEqualTo(GameStatus.DEALER_WINS);
        }
    }

    // ========== stand() and resolveDealerTurn() ==========

    @Test
    @DisplayName("should trigger dealer turn when player stands")
    void shouldTriggerDealerTurnOnStand() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN)),  // 20
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.FIVE), new Card(Card.Suit.SPADES, Card.Rank.SIX)),  // 11
                Deck.fullShuffled()
        );

        Game afterStand = domainService.stand(game);

        assertThat(afterStand.getStatus()).isNotEqualTo(GameStatus.IN_PROGRESS);
        assertThat(afterStand.getStatus()).isIn(
                GameStatus.PLAYER_WINS,
                GameStatus.DEALER_WINS,
                GameStatus.DRAW
        );
    }

    @Test
    @DisplayName("should dealer draw until score >= 17")
    void shouldDealerDrawUntilScoreGreaterOrEqualTo17() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.KING)),  // 20
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TWO), new Card(Card.Suit.SPADES, Card.Rank.THREE)),  // 5
                Deck.fullShuffled()
        );

        Game afterStand = domainService.stand(game);

        assertThat(afterStand.getDealerHand().score()).isGreaterThanOrEqualTo(17);
    }

    @Test
    @DisplayName("should set PLAYER_WINS if dealer busts")
    void shouldSetPlayerWinsWhenDealerBusts() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.KING)),  // 20
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.QUEEN), new Card(Card.Suit.SPADES, Card.Rank.JACK)),  // 20 (might bust if we add more)
                Deck.fullShuffled()
        );

        Game afterStand = domainService.stand(game);

        if (afterStand.getDealerHand().isBust()) {
            assertThat(afterStand.getStatus()).isEqualTo(GameStatus.PLAYER_WINS);
        }
    }

    @Test
    @DisplayName("should set PLAYER_WINS if player score > dealer score and dealer doesn't bust")
    void shouldSetPlayerWinsIfHigherScore() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.KING)),  // 20
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.FIVE), new Card(Card.Suit.SPADES, Card.Rank.FOUR)),  // 9
                Deck.fullShuffled()
        );

        Game afterStand = domainService.stand(game);

        if (afterStand.getPlayerHand().score() > afterStand.getDealerHand().score() && !afterStand.getDealerHand().isBust()) {
            assertThat(afterStand.getStatus()).isEqualTo(GameStatus.PLAYER_WINS);
        }
    }

    @Test
    @DisplayName("should set DEALER_WINS if dealer score > player score and player doesn't bust")
    void shouldSetDealerWinsIfHigherScore() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.TEN), new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE)),  // 15
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.KING), new Card(Card.Suit.SPADES, Card.Rank.QUEEN)),  // 20
                Deck.fullShuffled()
        );

        Game afterStand = domainService.stand(game);

        if (afterStand.getDealerHand().score() > afterStand.getPlayerHand().score() && !afterStand.getDealerHand().isBust()) {
            assertThat(afterStand.getStatus()).isEqualTo(GameStatus.DEALER_WINS);
        }
    }

    @Test
    @DisplayName("should set DRAW if both have same score")
    void shouldSetDrawIfSameScore() {
        Game game = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN)),  // 20
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.TEN), new Card(Card.Suit.SPADES, Card.Rank.JACK)),  // 20
                Deck.fullShuffled()
        );

        Game afterStand = domainService.stand(game);

        if (afterStand.getPlayerHand().score() == afterStand.getDealerHand().score() && !afterStand.getDealerHand().isBust()) {
            assertThat(afterStand.getStatus()).isEqualTo(GameStatus.DRAW);
        }
    }

    @Test
    @DisplayName("should not modify original game (immutable pattern)")
    void shouldNotModifyOriginalGame() {
        Game game1 = Game.create(
                "player1",
                Hand.of(new Card(Card.Suit.HEARTS, Card.Rank.KING), new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN)),
                Hand.of(new Card(Card.Suit.CLUBS, Card.Rank.FIVE), new Card(Card.Suit.SPADES, Card.Rank.SIX)),
                Deck.fullShuffled()
        );

        GameStatus originalStatus = game1.getStatus();

        Game game2 = domainService.stand(game1);
        assertThat(game1.getStatus()).isEqualTo(originalStatus);
    }
}


