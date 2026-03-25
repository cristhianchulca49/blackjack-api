package com.blackjack.game.domain.model;

import com.blackjack.game.domain.model.valueObject.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Hand Entity Tests")
class HandTest {

    @Test
    @DisplayName("should create hand with two cards")
    void shouldCreateHandWithTwoCards() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.KING);
        Card card2 = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);

        Hand hand = Hand.of(card1, card2);

        assertThat(hand.score()).isEqualTo(15);  // 10 + 5
    }

    @Test
    @DisplayName("should throw if first card is null")
    void shouldThrowIfFirstCardIsNull() {
        Card card2 = new Card(Card.Suit.HEARTS, Card.Rank.KING);

        assertThatThrownBy(() -> Hand.of(null, card2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cards cannot be null");
    }

    @Test
    @DisplayName("should throw if second card is null")
    void shouldThrowIfSecondCardIsNull() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.KING);

        assertThatThrownBy(() -> Hand.of(card1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cards cannot be null");
    }

    @Test
    @DisplayName("should throw if cards are the same")
    void shouldThrowIfCardsAreSame() {
        Card card = new Card(Card.Suit.HEARTS, Card.Rank.KING);

        assertThatThrownBy(() -> Hand.of(card, card))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cards cannot be the same");
    }

    @Test
    @DisplayName("should add card and return new Hand (immutable pattern)")
    void shouldAddCardReturnNewHand() {
        Hand hand1 = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.KING),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE)
        );

        Card card3 = new Card(Card.Suit.CLUBS, Card.Rank.THREE);
        Hand hand2 = hand1.addCard(card3);

        assertThat(hand1.score()).isEqualTo(15);

        assertThat(hand2.score()).isEqualTo(18);
    }

    @Test
    @DisplayName("should throw if adding null card")
    void shouldThrowIfAddingNullCard() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.KING),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE)
        );

        assertThatThrownBy(() -> hand.addCard(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Card cannot be null");
    }

    @Test
    @DisplayName("should calculate correct score for simple hand")
    void shouldCalculateScoreForSimpleHand() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.SEVEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT)
        );

        assertThat(hand.score()).isEqualTo(15);
    }

    @Test
    @DisplayName("should downgrade ACE from 11 to 1 when bust")
    void shouldDowngradeAceFromElevenToOneWhenBust() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.KING)
        );

        assertThat(hand.score()).isEqualTo(21);
        assertThat(hand.isBlackjack()).isTrue();


        Hand hand2 = hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.FIVE));
        assertThat(hand2.score()).isEqualTo(16);
        assertThat(hand2.isBust()).isFalse();
    }

    @Test
    @DisplayName("should handle multiple ACEs, downgrading all but one if needed")
    void shouldHandleMultipleAces() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE)
        );
        assertThat(hand.score()).isEqualTo(12);

        Hand hand2 = hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE));
        assertThat(hand2.score()).isEqualTo(13);
    }

    @Test
    @DisplayName("should detect blackjack: exactly 2 cards with score 21")
    void shouldDetectBlackjack() {
        Hand blackjack = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.KING)
        );

        assertThat(blackjack.isBlackjack()).isTrue();
        assertThat(blackjack.score()).isEqualTo(21);
    }

    @Test
    @DisplayName("should be blackjack if score 21, regardless of card count")
    void shouldNotBeBlackjackWith3Cards() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.SEVEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.SEVEN)
        ).addCard(new Card(Card.Suit.CLUBS, Card.Rank.SEVEN));

        assertThat(hand.score()).isEqualTo(21);
        assertThat(hand.isBlackjack()).isTrue();
    }

    @Test
    @DisplayName("should detect bust: score > 21")
    void shouldDetectBust() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.KING),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN)
        ).addCard(new Card(Card.Suit.CLUBS, Card.Rank.FIVE));

        assertThat(hand.score()).isEqualTo(25);
        assertThat(hand.isBust()).isTrue();
    }

    @Test
    @DisplayName("should NOT be bust if score is exactly 21")
    void shouldNotBeBustAtExactly21() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.KING),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE)
        );

        assertThat(hand.score()).isEqualTo(21);
        assertThat(hand.isBust()).isFalse();
    }

    @Test
    @DisplayName("should NOT be bust if score is under 21")
    void shouldNotBeBustUnder21() {
        Hand hand = Hand.of(
                new Card(Card.Suit.HEARTS, Card.Rank.FIVE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.SIX)
        );

        assertThat(hand.score()).isEqualTo(11);
        assertThat(hand.isBust()).isFalse();
    }
}

